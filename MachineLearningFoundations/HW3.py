import sys
import random
from HW1 import *
from HW2 import flips

import scipy as sci
import scipy.linalg
import scipy.io

import numpy as np

hw3_train_url = 'https://d396qusza40orc.cloudfront.net/ntumlone%2Fhw3%2Fhw3_train.dat'
hw3_test_url = 'https://d396qusza40orc.cloudfront.net/ntumlone%2Fhw3%2Fhw3_test.dat'
hw3_train = 'hw3_train.dat'
hw3_test = 'hw3_test.dat'

def function(X):
  return X[:,0]**2 + X[:,1]**2 - 0.6

def sigmod(s):
  return 1/(1+np.exp(-s))

def transform(X):
  x1 = X[:,0]
  x2 = X[:,1]
  return np.c_[X, x1*x2, x1**2, x2**2]

def generator(n):
  X = 2*np.random.rand(n,2)-1
  Y = np.sign(function(X))
  Y = flips(Y, 0.1)
  return X, Y.reshape(n,1)

def error01(eX, Y, w):
  return np.sum(np.sign(np.dot(eX, w)) != Y) / float(len(Y))

def linearRegression(X, Y):
  eX = expandX(X)
  pinvX = sci.linalg.pinv(eX) # k*m
  wlin = np.dot(pinvX, Y) # k*1
  return wlin, error01(eX, Y, wlin)

def Q13():
  avgEin = 0
  for i in range(100):
    X, Y = generator(1000)
    wlin, Ein = linearRegression(X, Y)
    avgEin = avgEin + Ein
  print avgEin/100

def Q14():
  Wlin = np.zeros((6,1))
  for i in range(100):
    X, Y = generator(1000)
    X = transform(X)
    wlin, Ein = linearRegression(X, Y)
    Wlin = Wlin + wlin
  print Wlin/100

def Q15():
  avgEout = 0
  for i in range(100):
    X, Y = generator(1000)
    X = transform(X)
    wlin, Ein = linearRegression(X, Y)
    Xt, Yt = generator(1000)
    eXt = expandX(transform(Xt))
    avgEout = avgEout + error01(eXt, Yt, wlin)
  print avgEout/100

def logisticRegression(X, Y, T, Eta):
  eX = expandX(X)
  m = eX.shape[0]
  k = eX.shape[1]
  wt = np.zeros((k,1))
  for i in range(T):
    grad = np.sum(sigmod(-Y*np.dot(eX, wt))*-Y*eX, 0)/m
    wt = wt - Eta*grad.reshape(k,1)
  return wt

def logisticRegressionSGD(X, Y, T, Eta):
  eX = expandX(X)
  m = eX.shape[0]
  k = eX.shape[1]
  wt = np.zeros((k,1))
  for i in range(T):
    n = i%m
    grad = sigmod(-Y[n]*np.dot(eX[n], wt))*Y[n]*eX[n]
    wt = wt + Eta*grad.reshape(k,1)
  return wt

def Q18(X, Y, XT, YT):
  w = logisticRegression(X, Y, 2000, 0.001)
  print error01(expandX(XT), YT, w)

def Q19(X, Y, XT, YT):
  w = logisticRegression(X, Y, 2000, 0.01)
  print error01(expandX(XT), YT, w)

def Q20(X, Y, XT, YT):
  w = logisticRegressionSGD(X, Y, 2000, 0.001)
  print error01(expandX(XT), YT, w)

def Q18_20():
  data = loadmat(hw3_train)
  X = data['X'] # m*k
  Y = data['Y'] # m*1

  dataT = loadmat(hw3_test)
  XT = dataT['X'] # t*k
  YT = dataT['Y'] # t*1

  Q18(X, Y, XT, YT)
  Q19(X, Y, XT, YT)
  Q20(X, Y, XT, YT)

def main():
  Q13()
  Q14()
  Q15()
  #saveWebData(hw3_train, hw3_train_url)
  #saveWebData(hw3_test, hw3_test_url)
  #sys.exit(0)
  Q18_20()

if __name__ == '__main__':
	main()