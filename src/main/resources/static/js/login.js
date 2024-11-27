document.getElementById("registerNow").addEventListener("click", function(event) {
    event.preventDefault();
    document.getElementById("loginForm").style.display = "none";
    document.getElementById("regForm").style.display = "flex";
});

document.getElementById("loginNow").addEventListener("click", function(event) {
    event.preventDefault();
    document.getElementById("loginForm").style.display = "flex";
    document.getElementById("regForm").style.display = "none";
});

function clearReg()
{
    document.getElementById('username').value = '';
    document.getElementById('userSirname').value = '';
    document.getElementById('email').value = '';
    document.getElementById('password').value = '';
    document.getElementById('confirmation').value = '';
    document.getElementById('confirmation').style.display='none';
}

function clearLogIn()
{
    document.getElementById('loginUsername').value = '';
    document.getElementById('loginPassword').value = '';
}

function showConfirmation() {
    var passwordInput = document.getElementById('password');
    var confirmationInput = document.getElementById('confirmation');

    if (passwordInput.value) {
        confirmationInput.style.display = 'block';
    } else {
        confirmationInput.style.display = 'none';
    }
}


function conf() {
    var passwordInput = document.getElementById('password');
    var confirmationInput = document.getElementById('confirmation');

    if (passwordInput.value == confirmationInput.value) {
        var form = document.getElementById("regForm");
        form.submit();
    } else {
        var error = document.getElementById("error-message");
        error.style.display = 'block';
    }
}
