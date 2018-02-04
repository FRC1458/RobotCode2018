function [sys] = ElevatorSystem(withCube)

    % Define Constants
    if (withCube); mass = 8.482; else; mass = 9.979; end
    sprocketRadius = 0.5;
    
    J = 0.000001 * mass * sprocketRadius * sprocketRadius; % Moment of Intertia
    R =  0.08955223881
    Kv = ((18730/60) * 6.28291852) /(12-R*0.7)
    Kt = 0.71/134

    % 18.7 lbs w/o cube , 22 lbs with cube

    % State x = [?; ?']
    % Input u = [V]
    
    % State-space model
    A = [0 0 
         0 math]; 
    B = [0
         more math];
    C = [1 0];
    D = 0;

    % Create system
    sys = ss(A, B, C, D);
end

