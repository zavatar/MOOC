syms u v
E = exp(u)+exp(2*v)+exp(u*v)+u^2-2*u*v+2*v^2-3*u-2*v;
Du = diff(E, u);
Dv = diff(E, v);

uv = [0,0];
for i = 1:5
	du = eval(subs(Du,[u,v],uv));
	dv = eval(subs(Dv,[u,v],uv));
	uv = uv - 0.01*[du,dv];
    % Q6
    if i == 1
        uv
    end
end
% Q7
eval(subs(E,[u,v],uv))
% Q8
taylor(E, [u,v], 'Order', 3)
% Q10
uv = [0,0];
for i = 1:5
	du = eval(subs(Du,[u,v],uv));
	dv = eval(subs(Dv,[u,v],uv));
    h = eval(subs(hessian(E,[u,v]),[u,v],uv));
	uv = uv - (h\[du;dv])';
end
eval(subs(E,[u,v],uv))
