
alert("Olá Mundo!");

function validarCampo(){
    const email = document.getElementById("email");
    const senha = document.getElementById("senha");
    const btn = document.getElementById("btn-logar");
    
    if(email.target.value.length > 0 && senha.target.value.length > 0);
        btn.disable = false;
    }