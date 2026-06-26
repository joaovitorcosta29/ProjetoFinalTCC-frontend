
alert("Olá Mundo!");

function validarCampo(){
    const email = document.getElementById("email");
    const senha = document.getElementById("senha");
    const btn = document.getElementById("btn-logar");
    
    if(email.value.length > 0 && senha.value.length > 0);
        btn.disable = false;
    }