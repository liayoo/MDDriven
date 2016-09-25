clear all
filename='150409_T1';
N=640;    % Number of samples
fs=128;   % Sampling frequency
xlRange='A1:A640'; % Range of values being read from Excel
V=xlsread(filename,xlRange); 
Vo=V>.011;  % True/False to filter out static
V=V.*Vo;   % Application of true/false vector
for n=2:N-2  % Two conditional loops to filter out random values
    if V(n-1)==0 & (V(n+1)==0 | V(n+2)==0)
        V(n)=0;
    end
end
for n=3:N-1
    if V(n+1)==0 & (V(n-1)==0 | V(n-2)==0)
        V(n)=0;
    end
end
pV=max(V)  % Max voltage reading from vector V
dP=-V/2.5;   % Positive convention for conversion from Voltage to pressure
r1=.012; %radius (in meters) of inlet (larger radius)
r2=.006; %radius (in meters) of bottleneck (smaller radius)
A1=pi*(r1^2);
A2=pi*(r2^2);
rho=1.2041; %kg/m^3
dP=dP/.000145; %Conversion from psi to pascal
Qm=sqrt(dP/(.5*rho*(1/(A1^2)-1/(A2^2))));
Q=(Qm*60)/.001; % Conversion from m^3/s to L/min
Qs=Qm/.001; % Conversion from m^3/s to L/s
t=1/128:1/128:5;
QsInt=Qs/fs; % Accounts for sampling frequency in integral
pQ=max(Q);  % Peak Flow
pQ
L=trapz(QsInt);
L
%L1=trapz(Qs,t);
%L1
plot(t,V)
Percent_Error=(L-3)/3*100