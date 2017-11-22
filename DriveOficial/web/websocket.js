window.onload = init;
var socket = new WebSocket("ws://localhost:8080/DriveOficial/actions");
socket.onmessage = onMessage;

function onMessage(event) {
    var usuario = JSON.parse(event.data);
    alert("Entro");
    if (device.action === "add") {
        printUsuarioNuevo(usuario);
    }
    if (device.action === "remove") {
        document.getElementById(device.id).remove();
        //device.parentNode.removeChild(device);
    }

}

function addUsuario(username, pass, bytes) {
    alert("Si entro");
    var UsuarioAction = {
        action: "add",
        username: username,
        pass: pass,
        bytes: bytes
    };
    alert( UsuarioAction.action +  UsuarioAction.username + UsuarioAction.pass );
    socket.send(JSON.stringify(UsuarioAction));
}

function removeDevice(element) {
    var id = element;
    var DeviceAction = {
        action: "remove",
        id: id
    };
    socket.send(JSON.stringify(DeviceAction));
}



function printDeviceElement(usuario) {
   alert(usuario.username);
}



function formSubmit() {
    var username = document.getElementById("username").value;
    var pass = document.getElementById("pass").value;
    var bytes=  document.getElementById("bytes").value;

    document.location.href = 'index.html';
    addUsuario(username, pass, bytes);
}

function init() {
    alert("init");
    hideForm();
}
