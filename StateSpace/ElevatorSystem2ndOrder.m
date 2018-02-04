function [sys] = ElevatorSystem(withCube)

    % Define Constants
    if (withCube); mass = 8.482; else; mass = 9.979; end
    sprocketRadius = 0.5;
    
    J = 0.000001*mass * sprocketRadius * sprocketRadius; % Moment of Intertia
    b = 0.00000001;  % Viscous Friction
    K = 1.30841; % Electromotive Force Constant / Motor Torque Constant
    R = 8.955;    % Electric Resistance
    L = 0.52985;  % Electric Inductance

    % 18.7 lbs w/o cube , 22 lbs with cube

    % State-space model
    A = [0 1      0
         0 -b/J   K/J
         0 -K/L  -R/L]; 
    B = [0
         0
         1/L];
    C = [360   0     0
         0     1     0
         0     0     1];
    D = 0;

    % Create system
    sys = ss(A, B, C, D);
end

