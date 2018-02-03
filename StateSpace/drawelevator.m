function DrawElevator(sprocketDiameter, angle, hasCube)

    % Calculate the height of the arm from motor angle
    height = sprocketDiameter * pi * angle / 360.0;
    if height > 72.0
        height = 72.0;
    end
    if height < 0.0
        height = 0.0;
    end

    % Baseline
    plot([-5 1.5],[0 0],'w','LineWidth',2)
    hold on
    
    % Title
    title(strcat('Height =', {' '}, num2str(round(height, 1)), ' inches'), 'Color', 'w', 'FontSize', 24)

    % Graph size
    daspect([1 1 1])
    xlim([-5 20]);
    ylim([-6 85]);
    
    set(gca,'Color','k','XColor','w','YColor','w')
    set(gcf,'Color','k')
    set(gcf,'InvertHardcopy','off')
    
    % Render cube
    if hasCube
        rectangle('Position', [5 height - 4.5 13 11], 'FaceColor',[0.94 0.85 0], 'EdgeColor', 'none')
    end
    
    % Gripper stage
    rectangle('Position', [1 height 10 2], 'FaceColor',[0.7 0.7 0.7], 'EdgeColor', 'none')
    rectangle('Position', [8 height+2 4 1], 'FaceColor',[0.5 0.0 0.0], 'EdgeColor', 'none') % Compliant wheel
    
    
    % Lower stage
    rectangle('Position', [-1.5 0 3 38], 'FaceColor',[0.5 0.5 0.5], 'EdgeColor', 'none')
    
    % Moving stage
    rectangle('Position', [-1 38 2 height/2], 'FaceColor',[0.5 0.5 0.5], 'EdgeColor', 'none')

    
    % End rendering
    drawnow
    hold off
