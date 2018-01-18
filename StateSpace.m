% Define Constants
J = 0.01; % Moment of Intertia
b = 0.1;  % Viscous Friction
K = 0.01; % Electromotive Force Constant / Motor Torque Constant
R = 1;    % Electric Resistance
L = 0.5;  % Electric Inductance

% State-space model
A = [-b/J   K/J
     -K/L  -R/L];
B = [0
    1/L];
C = [1   0];
D = 0;

% Create system
sys = ss(A, B, C, D);