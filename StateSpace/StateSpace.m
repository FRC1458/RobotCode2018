clear all, close all, clc

% Define Constants
J = 8.0; % Moment of Intertia
b = 0.3;  % Viscous Friction
K = 3.0; % Electromotive Force Constant / Motor Torque Constant
R = 1.0;    % Electric Resistance
L = 0.01;  % Electric Inductance

% 18.7 lbs w/o cube , 22 lbs with cube

% State-space model
A = [0 1      0
     0 -b/J   K/J
     0 -K/L  -R/L]; 
B = [0
     0
     1/L];
C = [1   0   0];
D = 0;

% Create system
sys = ss(A, B, C, D);

%[u,t] = gensig('square',5,30,0.1);
%lsim(sys,u,t)

Kp = 1;
for i = 1:3
    con(:,:,i) = pid(Kp, 0.0, 5.0);
    Kp = Kp + 10;
end
sys_cl = feedback(con*sys, 1);
t = 0:0.001:10.0;
set(gco, 'LineWidth', 8)  
step(sys_cl(:,:,1), sys_cl(:,:,2), sys_cl(:,:,3), t)
ylabel('Position, \theta (radians)')
title('Response to a Step Reference with Different Values of K_p')
legend('Kp = 1',  'Kp = 11',  'Kp = 21')