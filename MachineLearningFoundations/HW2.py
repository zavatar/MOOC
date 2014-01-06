import sys
import random
from HW1 import saveWebData
from scipy.io import loadmat

import numpy as np

hw2_train_url = 'https://d396qusza40orc.cloudfront.net/ntumlone%2Fhw2%2Fhw2_train.dat'
hw2_test_url = 'https://d396qusza40orc.cloudfront.net/ntumlone%2Fhw2%2Fhw2_test.dat'
hw2_train = 'hw2_train.dat'
hw2_test = 'hw2_test.dat'

def flips(Y, p):
  index = np.random.rand(len(Y))<p
  Y[index] = -Y[index]
  return Y

def generator(n):
  X = 2*np.random.rand(n)-1
  Y = np.sign(X)
  Y = flips(Y, 0.2)
  return X, Y

def desicionStump(X, Y):
  Ein = len(Y)
  Theta = 0
  S = 1
  x = list(X)
  x.sort()
  for i in range(len(Y)+1):
    if i == 0: theta = x[0]-1
    elif i == len(Y): theta = x[-1]+1
    else: theta = (x[i-1] + x[i]) / 2
    Ein1 = len(Y[np.sign(X - theta) != Y])
    Ein2 = len(Y[-np.sign(X - theta) != Y])
    if Ein2 < Ein1:
      ein = Ein2
      s = -1
    else:
      ein = Ein1
      s = 1
    if ein < Ein:
      Ein = ein
      Theta = theta
      S = s
  Eout = 0.5 + 0.3 * S * (abs(Theta)-1)
  return Theta, S, float(Ein)/len(Y), Eout

def Q17_18():
  avgEin = 0
  avgEout = 0
  for i in range(5000):
    X, Y = generator(20)
    theta, s, Ein, Eout = desicionStump(X, Y)
    avgEin = avgEin + Ein
    avgEout = avgEout + Eout
  print avgEin/5000, avgEout/5000

def Q19_20():
  data = loadmat(hw2_train)
  X = data['X'] # m*k
  Y = data['Y'] # m*1

  k = X.shape[1]
  Theta = [0]*k
  S = [0]*k
  optEin = 1
  opti = 0
  for i in range(k):
    Theta[i], S[i], Ein, Eout = desicionStump(X[:,i], Y.T[0])
    if Ein < optEin:
      optEin = Ein
      opti = i
  print optEin

  dataT = loadmat(hw2_test)
  XT = dataT['X'] # t*k
  YT = dataT['Y'] # t*1

  Eout = len(YT.T[0][S[opti]*np.sign(XT[:,opti] - Theta[opti]) != YT.T[0]])/float(len(YT))
  print Eout
  
def main():
  #saveWebData(hw2_train, hw2_train_url)
  #saveWebData(hw2_test, hw2_test_url)
  #sys.exit(0)

  #Q17_18()

  Q19_20()

if __name__ == '__main__':
  main()