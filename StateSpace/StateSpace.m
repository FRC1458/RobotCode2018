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
G = ss(A, B, C, D);
C = pid(0.1, 0, 0);
closedLoop = feedback(G*C,1);