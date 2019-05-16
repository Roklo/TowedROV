%A=readtable('ExportedData.csv')
A=insideTempCameraHouse;
figure;
x= A{:,2};
y=A{:,3};
x=x/60;
plot(x,y)
xlabel('Time (min)'), ylabel('Celcius')
legend('Temperature', 'Location', 'NorthWest')
title('Internal temp above 12->5V trafo')


grid on

