syms delt N dvc epsilon mH(n)
GrowthFun = mH(n) == n^dvc;
VCBound = delt == 4*(2*N)^dvc*exp(-(epsilon^2)/8*N);

epsilonsym = solve(VCBound, epsilon);
deltsym = solve(VCBound, delt);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Q3 
%{
dvc = 10;
epsilon = 0.05;
delt = 0.05;
Error = 1;
resultN = 0;

N = 420000;
e = abs(eval(subs(deltsym)) - delt);
if e < Error
    resultN = N;
    Error = e;
end
N = 440000;
e = abs(eval(subs(deltsym)) - delt);
if e < Error
    resultN = N;
    Error = e;
end
N = 460000;
e = abs(eval(subs(deltsym)) - delt);
if e < Error
    resultN = N;
    Error = e;
end
N = 480000;
e = abs(eval(subs(deltsym)) - delt);
if e < Error
    resultN = N;
    Error = e;
end
resultN;
%}
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Q4
dvc = 50;
delt = 0.05;
N = 5;

% VC
%{
N = 5:100:10000;
plot(N, eval(epsilonsym), '-');
%}

% Parrondo and Van den Broek
%{
PVBound = epsilon == sqrt(1/N*(2*epsilon + log(6*(2*N)^dvc/delt)));
epsilonsym1 = solve(subs(PVBound), epsilon);
eval(epsilonsym1)
%}

% Devroye
%{
DBound = epsilon == sqrt(0.5/N*(4*epsilon*(1+epsilon) + log(4*(N^2)^dvc/delt)));
epsilonsym2 = solve(subs(DBound), epsilon);
eval(epsilonsym2)
%}

% Rademacher Penalty Bound
%{
RBound = epsilon == sqrt(2*log(2*N*N^dvc)/N) + sqrt(2*log(1/delt)/N) + 1/N;
epsilonsym3 = solve(subs(RBound), epsilon);
eval(epsilonsym3)
%}