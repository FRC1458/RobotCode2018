function [J, R, Kv, Kt] = Constants(withCube)

    % Define Constants
    if (withCube); mass = 9.979; else; mass = 8.482; end
    sprocketRadius = 0.5;
    
    J  = mass * sprocketRadius * sprocketRadius; % Moment of Intertia
    R  = 0.08955223881;
    Kv = ((18730/60) * 6.28291852) / (12 - R*0.7);
    Kt = 0.71/134;
end

