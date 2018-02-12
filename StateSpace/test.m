
mass = 8.482;
sprocketRadius = 0.5;
J  = mass * sprocketRadius * sprocketRadius; % Moment of Intertia
R  = 0.08955223881;
Kv = ((18730/60) * 6.28291852) / (12 - R*0.7);
Kt = 0.71/134;

A = [0 1
     0 (-Kt*Kv)/(J*R)]; 
B = [0
     (Kt)/(J*R)];
C = [1 0
     0 1];
D = 0;

% Create system
sys = ss(A, B, C, D)

rank(ctrb(sys))