import sys
import random

import numpy as np

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
  for theta in X:
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
  return Theta, S, float(Ein)/len(Y)

def Q17_18():
  avgEin = 0
  avgEout = 0
  for i in range(5000):
    X, Y = generator(20)
    theta, s, Ein = desicionStump(X, Y)
    avgEin = avgEin + Ein
    varX, varY = generator(20)
    Eout = len(varY[s*np.sign(varX - theta) != varY])/float(len(varY))
    avgEout = avgEout + Eout
  print avgEin/5000, avgEout/5000

def main():
  Q17_18()

if __name__ == '__main__':
  main()