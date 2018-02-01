function drawelevator(sprocketDiameter, angle, hasCube)

    height = sprocketDiameter * pi * angle / 360.0;
    
    if height > 72.0
        height = 72.0
    end
    
    if height < 0.0
        height = 0.0
    end

    plot([-5 20],[0 0],'w','LineWidth',2)
    hold on

    daspect([1 1 1])
    xlim([-5 20]);
    ylim([0 85]);
    set(gca,'Color','k','XColor','w','YColor','w')
    set(gcf,'Position',[10 900 800 400])
    set(gcf,'Color','k')
    set(gcf,'InvertHardcopy','off')   
    
    rectangle('Position',[-1.5 0 3 36], 'FaceColor',[0.5 0.5 0.5], 'EdgeColor', 'none')
    rectangle('Position',[-1 36 2 height/2], 'FaceColor',[0.65 0.65 0.65], 'EdgeColor', 'none')

    % box off
    drawnow
    hold off
