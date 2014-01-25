import sys
import math
from HW3 import *

hw4_train_url = 'https://d396qusza40orc.cloudfront.net/ntumlone%2Fhw4%2Fhw4_train.dat'
hw4_test_url = 'https://d396qusza40orc.cloudfront.net/ntumlone%2Fhw4%2Fhw4_test.dat'
hw4_train = 'hw4_train.dat'
hw4_test = 'hw4_test.dat'

def split(X, Y, n):
  return X[:n,:], Y[:n,:], X[n:,:], Y[n:,:]

def splitFolds(X, Y, nfold, i):
  foldsize = X.shape[0]/nfold
  return np.r_[X[:(i*foldsize),:], X[((i+1)*foldsize):,:]],\
    np.r_[Y[:(i*foldsize),:], Y[((i+1)*foldsize):,:]],\
    X[(i*foldsize):((i+1)*foldsize),:],\
    Y[(i*foldsize):((i+1)*foldsize),:]

def ridgeRegression(X, Y, Lambda):
  eX = expandX(X)
  wreg = np.dot(np.dot(sci.linalg.inv(np.dot(eX.T, eX)+\
    Lambda*np.eye(eX.shape[1])), eX.T), Y)
  return wreg, error01(eX, Y, wreg)

def Q13_15(X, Y, XT, YT):
  wreg, Ein = ridgeRegression(X, Y, 10)
  print Ein, error01(expandX(XT), YT, wreg)

  minEin = 1
  minEout = 1
  for log10lambda in range(2, -11, -1):
    wreg, Ein = ridgeRegression(X, Y, math.pow(10, log10lambda))
    Eout = error01(expandX(XT), YT, wreg)
    if minEin > Ein:
      Q14log10lambda = log10lambda
      minEin = Ein
      Q14Eout = Eout
    if minEout > Eout:
      Q15log10lambda = log10lambda
      Q15Ein = Ein
      minEout = Eout
  print Q14log10lambda, minEin, Q14Eout
  print Q15log10lambda, Q15Ein, minEout

def Q16_18(X, Y, XT, YT):
  minEtrain = 1
  minEval = 1
  Xtrain, Ytrain, Xval, Yval = split(X, Y, 120)
  for log10lambda in range(2, -11, -1):
    wreg, Etrain = ridgeRegression(Xtrain, Ytrain, math.pow(10, log10lambda))
    Eval = error01(expandX(Xval), Yval, wreg)
    Eout = error01(expandX(XT), YT, wreg)
    if minEtrain > Etrain:
      Q16log10lambda = log10lambda
      minEtrain = Etrain
      Q16Eval = Eval
      Q16Eout = Eout
    if minEval > Eval:
      Q17log10lambda = log10lambda
      Q17Etrain = Etrain
      minEval = Eval
      Q17Eout = Eout
  print Q16log10lambda, minEtrain, Q16Eval, Q16Eout
  print Q17log10lambda, Q17Etrain, minEval, Q17Eout

  optimalLambda = math.pow(10, Q17log10lambda)
  wreg, Ein = ridgeRegression(X, Y, optimalLambda)
  print Ein, error01(expandX(XT), YT, wreg)

def Q19_20(X, Y, XT, YT, nfold):
  minEcv = 1
  for log10lambda in range(2, -11, -1):
    Eval = 0.0
    for i in range(nfold):
      Xtrain, Ytrain, Xval, Yval = splitFolds(X, Y, nfold, i)
      wreg, Etrain = ridgeRegression(Xtrain, Ytrain, math.pow(10, log10lambda))
      Eval += error01(expandX(Xval), Yval, wreg)
    Ecv = Eval/nfold
    if minEcv > Ecv:
      Q19log10lambda = log10lambda
      minEcv = Ecv
  print Q19log10lambda, minEcv

  optimalLambda = math.pow(10, Q19log10lambda)
  wreg, Ein = ridgeRegression(X, Y, optimalLambda)
  print Ein, error01(expandX(XT), YT, wreg)

def main():
  #saveWebData(hw4_train, hw4_train_url)
  #saveWebData(hw4_test, hw4_test_url)
  #sys.exit(0)

  data = loadmat(hw4_train)
  X = data['X'] # m*k
  Y = data['Y'] # m*1

  dataT = loadmat(hw4_test)
  XT = dataT['X'] # t*k
  YT = dataT['Y'] # t*1

  #Q13_15(X, Y, XT, YT)
  #Q16_18(X, Y, XT, YT)
  Q19_20(X, Y, XT, YT, 5)

if __name__ == '__main__':
	main()