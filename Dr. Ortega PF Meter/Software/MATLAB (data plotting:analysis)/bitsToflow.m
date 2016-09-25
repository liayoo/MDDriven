function [ flow ] = bitsToflow(bits, cal)

  %c1 = 0.004887585533; for earlier versions
  c1 = 0.0048828125;
  c2 = 0.0079066947;    %shouldn't change
  c4 = cal+1;
  
  %c4 = [cal+1,cal+2,cal+3,cal-1,cal-2,cal-3]; for earlier versions

  if( (c4-bits)>=0) %any(bits==c4) ) this was changed for oversampling code
    bits = cal;
  end
  
  a = c1*(bits - cal);       %[V]
  
  if(a < 0)                         
      flow = -sqrt(-a) * c2; %[m^3/s]
  else
      flow = sqrt(a) * c2;
  end

end

