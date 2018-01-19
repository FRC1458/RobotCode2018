% Define Constants
J = 0.2; % Moment of Intertia
b = 0.1;  % Viscous Friction
K = 3.0; % Electromotive Force Constant / Motor Torque Constant
R = 1.0;    % Electric Resistance
L = 0.01;  % Electric Inductance

% State-space model
A = [-b/J   K/J
     -K/L  -R/L];
B = [0
    1/L];
C = [1   0];
D = 0;

% Create system
sys = ss(A, B, C, D);

[u,t] = gensig('square',5,30,0.1);
lsim(sys,u,t)

