clear all, close all, clc

withCube = false;

[J, R, Kv, Kt] = Constants(withCube);
[sys] = ElevatorSystem(withCube);

A = [0 1
     0 (-Kt*Kv)/(J*R)]; 
B = [0
     (Kt)/(J*R)];
C = [1 0
     0 1];
D = 0;
 
% Error cost
Q = [30 0
     0 3];

% Movement cost
R = 0.0001; 

K = lqr(A,B,Q,R)

  
t = 0:0.001:5.0;
%sys_cl = ss(A-B*K,[0; 1/3.39],C,D);
sys_cl = ss(A - B*K, B, C, D);
step(sys_cl, t, stepDataOptions('StepAmplitude', 1002))

