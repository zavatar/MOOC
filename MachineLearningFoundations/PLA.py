import sys
import urllib2
import random
import argparse

import numpy as np
from numpy import linalg as LA
from scipy.io import loadmat
from scipy.io import savemat
import matplotlib.pyplot as plt
import matplotlib.animation as animation


hw1_15_train_url = 'https://d396qusza40orc.cloudfront.net/ntumlone%2Fhw1%2Fhw1_15_train.dat'
hw1_18_train_url = 'https://d396qusza40orc.cloudfront.net/ntumlone%2Fhw1%2Fhw1_18_train.dat'
hw1_18_test_url = 'https://d396qusza40orc.cloudfront.net/ntumlone%2Fhw1%2Fhw1_18_test.dat'
hw1_15_train = 'hw1_15_train.dat'
hw1_18_train = 'hw1_18_train.dat'
hw1_18_test = 'hw1_18_test.dat'

def download(url):
  try:
    webdata = urllib2.urlopen(url)
  except urllib2.HTTPError, err:
    if err.code == 404:
      print "Page not found!"
    elif err.code == 403:
      print "Access denied!"
    else:
      print "Something happened! Error code", err.code
    sys.exit(0)
  except urllib2.URLError, err:
    print "Some other error happened:", err.reason
    sys.exit(0)
  return webdata

def saveWebData(fname, url):
  X = []
  Y = []
  for line in download(url):
    num_str = line.split()
    X.append(map(float, num_str[0:-1]))
    Y.append(int(num_str[-1]))
  savemat(fname, {'X': np.matrix(X), 'Y': np.matrix(Y).T})

def sign(t):
  if t > 0: return 1
  else: return -1

def _countMistake(X, Y, w):
  count = 0
  for i in range(X.shape[0]):
    if sign(np.dot(w, X[i])) != Y[i]: count = count+1
  return count

def countMistake(X, Y, w):
  X = np.concatenate((np.ones((X.shape[0],1)), X), 1)
  return _countMistake(X, Y, w)

def PLA(X, Y, u = 1, index = [], limit = None):
  m = X.shape[0]
  k = X.shape[1]
  if index==[]: index = range(m)
  X = np.concatenate((np.ones((m,1)), X), 1)
  wt = np.zeros(k+1)
  wts = [wt]
  if limit != None:
    updates = 0
    pocket = 0
    mistakes = m
  while True:
    for i in index:
      if sign(np.dot(wt, X[i])) != Y[i]:
        wt = wt + u*Y[i]*X[i]
        wts.append(wt)
    else: break
    if limit != None:
      updates = updates+1
      newmis = _countMistake(X, Y, wt)
      if newmis < mistakes:
        mistakes = newmis
        pocket = len(wts)-1
        if mistakes == 0: break
      if updates >= limit: break
  if limit == None: pocket = len(wts)-1
  return wts, pocket

def Q15(X, Y, u = 1, index = []):
  wts, pocket = PLA(X, Y, u = u, index = index)
  print LA.norm(wts[-1])
  return len(wts)

def Q16(X, Y, n = 2000, u = 1):
  index = range(X.shape[0])
  sumt = 0
  avgw = np.zeros(X.shape[1]+1)
  for i in range(n):
    random.shuffle(index)
    wts, pocket = PLA(X, Y, u = u, index = index)
    avgw = avgw + wts[-1]
    sumt = sumt + len(wts)
  print LA.norm(avgw/n)
  return sumt/n

def Q17(X, Y, n = 2000, u = 1):
  return Q16(X, Y, n, u)

def test2D(X, Y, u = 1):
  m = min(X.shape[0], 50)
  k = X.shape[1]
  x = np.ones((m,2))
  y = np.zeros(m)
  P = 1000
  R = 0
  wf = np.array( [-random.random(),random.uniform(-1, 1),1] )
  print wf
  index = range(X.shape[0])
  random.shuffle(index)
  for i in range(m):
    x[i] = X[index[i],0:2]
    _x = np.hstack(([1],x[i]))
    wfx = np.dot(wf, _x)
    y[i] = sign(wfx)
    P = min(P, y[i]*wfx)
    R = max(R, LA.norm(_x))

  print P, R, P/LA.norm(wf)
  wts, pocket = PLA(x, y, u)
  T = len(wts)
  print wts[-1]
  print T

  if wts[-1][2] == 0: sys.exit(0)
  t = np.arange(0, 1, 0.01)
  plt.plot(t, -wf[1]/wf[2]*t-wf[0]/wf[2], '-')
  plt.plot(x[y==1,0], x[y==1,1], 'o', x[y==-1,0], x[y==-1,1], '+')
  plt.plot(t, -wts[-1][1]/wts[-1][2]*t-wts[-1][0]/wts[-1][2], '-')
  plt.plot(x[y==1,0], x[y==1,1], 'o', x[y==-1,0], x[y==-1,1], '+')
  plt.show()

  plt.plot(range(T), np.dot(wts,wf), '-')
  plt.plot(range(T), np.dot(wts,wf)/(LA.norm(wts,axis=1)*LA.norm(wf)), '-')
  #plt.plot(range(T), LA.norm(wts,axis=1), '-')
  #plt.plot(range(T), np.asarray(range(T))**(1./2)*P/LA.norm(wf)/R, '-')
  plt.show()

def Q15_17():
  data = loadmat(hw1_15_train)
  X = data['X'] # m*k
  Y = data['Y'] # m*1

  #test2D(X, Y)

  print Q15(X, Y)
  print Q16(X, Y, 2000)
  print Q17(X, Y, 2000, 0.5)

def Q18(X, Y, XT, YT, n = 2000):
  index = range(X.shape[0])
  error = 0
  for i in range(n):
    random.shuffle(index)
    wts, pocket = PLA(X, Y, index = index, limit = 50)
    mistakes = countMistake(XT, YT, wts[pocket])
    error = error + float(mistakes)/XT.shape[0]
  return error/n

def Q19(X, Y, XT, YT, n = 2000):
  index = range(X.shape[0])
  error = 0
  for i in range(n):
    random.shuffle(index)
    wts, pocket = PLA(X, Y, index = index, limit = 50)
    mistakes = countMistake(XT, YT, wts[-1])
    error = error + float(mistakes)/XT.shape[0]
  return error/n

def Q20(X, Y, XT, YT, n = 2000):
  index = range(X.shape[0])
  error = 0
  for i in range(n):
    random.shuffle(index)
    wts, pocket = PLA(X, Y, index = index, limit = 100)
    mistakes = countMistake(XT, YT, wts[pocket])
    error = error + float(mistakes)/XT.shape[0]
  return error/n

def Q18_20():
  data = loadmat(hw1_18_train)
  X = data['X'] # m*k
  Y = data['Y'] # m*1

  dataT = loadmat(hw1_18_test)
  XT = dataT['X'] # t*k
  YT = dataT['Y'] # t*1

  print Q18(X, Y, XT, YT, 200)
  print Q19(X, Y, XT, YT, 200)
  print Q20(X, Y, XT, YT, 200)


def main():
  #saveWebData(hw1_15_train, hw1_15_train_url)
  #saveWebData(hw1_18_train, hw1_18_train_url)
  #saveWebData(hw1_18_test, hw1_18_test_url)
  #sys.exit(0)
  
  Q15_17()

  Q18_20()

if __name__ == '__main__':
  main()