function [sys] = ElevatorSystem(withCube)

    [J, R, Kv, Kt] = Constants(withCube)

    % State x = [t; t'] (t = angle)
    % Input u = [V]
    
    % State-space model
    A = [0 1
         0 (-Kt*Kv)/(J*R)]; 
    B = [0
         (Kt)/(J*R)];
    C = [1 0
         0 1];
    D = 0;

    % Create system
    sys = ss(A, B, C, D);
end

