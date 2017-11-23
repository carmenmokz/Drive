window.onload = init;
var socket = new WebSocket("ws://localhost:8080/DriveOficial/actions");
socket.onmessage = onMessage;
var users;
function onMessage(event) {
    var usuario = JSON.parse(event.data);
    if (usuario.action === "add") {
        getUsers();
    }
    if (usuario.action === "remove") {
        //document.getElementById(device.id).remove();
        //device.parentNode.removeChild(device);
    }
    if (usuario.action === "getUsers") {
        users = usuario.users;
    }
    if (usuario.action === "getMainFolder") {

       refreshFolders(usuario);
       refreshFiles(usuario);
       
  
    }
    
    if (usuario.action === "getShareFolder") {
       refreshFolders(usuario);
       refreshFiles(usuario);
    }
    if (usuario.action === "changeFolder") {
      
       refreshFolder(usuario);
       refreshFolders(usuario);
       refreshFiles(usuario);
    }
     if (usuario.action === "view") {
      
      
       refreshView(usuario);
    }
    if(usuario.action==="verFolder"){
        verFolder(usuario);

    }
    if(usuario.action==="verFile"){
        verFile(usuario);

    }


}

function verFolder(usuario){
    if (confirm('El directorio ya existe. ¿Desea caerle encima?')) {
            var UsuarioAction = {
                action: "deleteFolder",
                username: document.getElementById("menu-name").innerHTML,
                file: usuario.nombre,
                dir: document.getElementById("current-folder").innerHTML,

            };
            socket.send(JSON.stringify(UsuarioAction));
             var UsuarioAction = {
                action: "addFolder",
                username: document.getElementById("menu-name").innerHTML,
                dir: document.getElementById("current-folder").innerHTML,
                name:usuario.nombre
            };

            socket.send(JSON.stringify(UsuarioAction));
        } else {
            alert('Why did you press cancel? You should have confirmed');
        }
}
function verFile(usuario){
    if (confirm('El archivo ya existe. ¿Desea caerle encima?')) {
            var UsuarioAction = {
                action: "deleteFile",
                username: document.getElementById("menu-name").innerHTML,
                file: usuario.nombre,
                dir: document.getElementById("current-folder").innerHTML,
                ext: usuario.ext
            };
            socket.send(JSON.stringify(UsuarioAction));
              var UsuarioAction = {
                    action: "addFile",
                    username: document.getElementById("menu-name").innerHTML,
                    dir:document.getElementById("current-folder").innerHTML,
                    name:usuario.nombre,
                    cont:usuario.cont,
                    ext:usuario.ext
                };

                socket.send(JSON.stringify(UsuarioAction));
        } else {
            alert('Why did you press cancel? You should have confirmed');
        }
}
function getUsers() {
   
    var UsuarioAction = {
        action: "getUsers"
     
    };
  
    socket.send(JSON.stringify(UsuarioAction));

}
function getMainFolder(){
    var UsuarioAction = {
            action: "getMainFolder",
            username: document.getElementById("menu-name").innerHTML
          
        };
   
    socket.send(JSON.stringify(UsuarioAction));

}
function getShareFolder(){
    var UsuarioAction = {
            action: "getShareFolder",
            username: document.getElementById("menu-name").innerHTML
          
        };

    socket.send(JSON.stringify(UsuarioAction));
}

function isUserAlready(username){
    var userC=users;  
    var i;
    var exist = 0; 
    for(i in userC){
        if ((userC[i].username).localeCompare(username)===0){
            alert("El Usuario Escogido, ya existe.");
            exist=1;
            break;
        }
    }    
    return exist; 
}


function addUsuario(username, pass, bytes) {
    getUsers();
    var i;
    var exist=0;
    var userC=users;
   
  
 
    exist = isUserAlready(username); 

    if(exist===0||userC===null){
        var UsuarioAction = {
            action: "add",
            username: username,
            pass: pass,
            bytes: bytes
        };

        socket.send(JSON.stringify(UsuarioAction));
        alert("Se ha creado el Usuario: "+ username);
    }
}
function removeDevice(element) {
    var id = element;
    var DeviceAction = {
        action: "remove",
        id: id
    };
    socket.send(JSON.stringify(DeviceAction));
}
function changeFolder(name)
{
    var UsuarioAction = {
            action: "changeFolder",
            username: document.getElementById("menu-name").innerHTML,
            folder: document.getElementById("current-folder").innerHTML+name
          
        };

    socket.send(JSON.stringify(UsuarioAction));
    var current=document.getElementById("current-folder").innerHTML+name;
    alert(current);
    
    
}





function formSubmit() {
    var username = document.getElementById("username").value;
    var pass = document.getElementById("pass").value;
    var bytes=  document.getElementById("bytes").value;

    document.location.href = 'index.html';
    addUsuario(username, pass, bytes);
    
}

function addFolder() {
    
    var UsuarioAction = {
        action: "addFolder",
        username: document.getElementById("menu-name").innerHTML,
        dir: document.getElementById("current-folder").innerHTML,
        name:document.getElementById("name-folder").value
    };
    
    socket.send(JSON.stringify(UsuarioAction));
    addFolderDisp.style.display = "none";

}
function addFile() {
   
    var UsuarioAction = {
        action: "addFile",
        username: document.getElementById("menu-name").innerHTML,
        dir:document.getElementById("current-folder").innerHTML,
        name:document.getElementById("name-file").value,
        cont:document.getElementById("cont-file").value,
        ext:document.getElementById("ext-file").value
    };
  
    socket.send(JSON.stringify(UsuarioAction));
    addFileDisp.style.display = "none";
}

function copyAll(){
    var origin =document.getElementById("origin-copy").value;
    var destiny =document.getElementById("destiny-copy").value;
    
    
    alert("Entre");
    alert("Tipo "+typeCopy);
    alert(origin);
    alert(destiny);
    var UsuarioAction = {
        action: "copy",
        username: document.getElementById("menu-name").innerHTML,
        origin: origin,
        destiny: destiny,
        type: typeCopy
    };
    alert("Si la guarde:"+UsuarioAction.type);
    socket.send(JSON.stringify(UsuarioAction));
    copy.style.display = "none";
}
function deleteFile(){
    var tbody = document.getElementById('file-system');
    var rowLength = tbody.rows.length;
    alert(rowLength);
    for(var i=0; i<rowLength; i+=1){   
      var row = tbody.rows[i];
      var state=document.getElementById("ck"+row.cells[2].innerHTML).checked;
      
      if(state===true){
        [file,ext]=row.cells[1].innerHTML.split(".");
        alert(ext);
        var UsuarioAction = {
        action: "deleteFile",
        username: document.getElementById("menu-name").innerHTML,
        file: row.cells[2].innerHTML,
        dir: document.getElementById("current-folder").innerHTML,
        ext: ext
    };
    socket.send(JSON.stringify(UsuarioAction));
          
     }
      
   }
    
}

function deleteFolder(){
    var tbody = document.getElementById('file-system');
    var rowLength = tbody.rows.length;
    alert(rowLength);
    for(var i=0; i<rowLength; i+=1){   
      var row = tbody.rows[i];
      var state=document.getElementById("ck"+row.cells[2].innerHTML).checked;
      
      if(state===true){
        var UsuarioAction = {
        action: "deleteFolder",
        username: document.getElementById("menu-name").innerHTML,
        file: row.cells[2].innerHTML,
        dir: document.getElementById("current-folder").innerHTML,
        
    };
    socket.send(JSON.stringify(UsuarioAction));
          
     }
      
   }
    
}

function moveAll(){
   alert("Entro al move");
   var destiny =document.getElementById("destiny-move").value;
   var tbody = document.getElementById('file-system'); 
   var rowLength = tbody.rows.length;
   alert("Mo llega");
   for(var i=0; i<rowLength; i+=1){   
      alert("Entro al for");
      var row = tbody.rows[i];
      var state=document.getElementById("ck"+row.cells[2].innerHTML).checked;
      if(state===true){
       
        var file;
        if(typeMove===0){
            var file=row.cells[1].innerHTML;
        }else{
            var file=row.cells[2].innerHTML;
        }
    
    
        var UsuarioAction = {
        action: "move",
        username: document.getElementById("menu-name").innerHTML,
        dir:document.getElementById("current-folder").innerHTML,
        file: file,
        destiny: destiny,
        type: typeMove
    }}
     socket.send(JSON.stringify(UsuarioAction));
    };
  
  
    
    alert("Sale");
    move.style.display = "none";
}

function shareDirectory(){
    alert("quak1"); 
    var username = document.getElementById("menu-name").innerHTML; 
    alert(username);
    var toUser = document.getElementById("user-share").value; 
    var path = document.getElementById("current-folder").innerHTML; 
    alert(toUser);
    var tbody = document.getElementById('file-system'); 
    var rowLength = tbody.rows.length;
    alert("Mo llega");
    for(var i=0; i<rowLength; i+=1){   
        alert("Entro al for");
    var row = tbody.rows[i];
    var state=document.getElementById("ck"+row.cells[2].innerHTML).checked;
      
    if(state===true){
       
      
        var file=row.cells[2].innerHTML;
        
        var finalpath=path+"/"+file;
        alert(finalpath);
    
        var exist = 0; 
        exist = isUserAlready(toUser);

        if(exist===1){
            var share = {
                action: "shareDir",
                username: username,
                path: finalpath, 
                toUser: toUser

            };
            socket.send(JSON.stringify(share));
            alert("Se solicitó compartir archivo: De: "+ username + " A:" + toUser+ " Archivo: " + nameFile + " Path: "+ currentPath);
            }
        }
    };
    
}

function shareFile(){
    alert("quak 1");
    var username = document.getElementById("menu-name").innerHTML; 
    alert(username);
     
    
    var currentPath = document.getElementById("current-folder").innerHTML; 
    alert(currentPath);
    var toUser = document.getElementById("user-share").value; 
    alert(toUser);
    var tbody = document.getElementById('file-system'); 
    alert(tbody);
    var rowLength = tbody.rows.length;
    alert("Mo llega");
    for(var i=0; i<rowLength; i+=1){   
        
        var row = tbody.rows[i];
        alert(row);
        var state= document.getElementById("ck"+row.cells[2].innerHTML).checked;

        if(state===true){
            var file = row.cells[1].innerHTML;
            alert(file); 
            var exist = 0; 
            exist = isUserAlready(toUser); 
            if(exist===1){
                var share = {
                    action: "shareFile",
                    username: username,
                    file: file, 
                    currentPath: currentPath, 
                    toUser: toUser
                };
            socket.send(JSON.stringify(share));
            alert("Se solicitó compartir archivo: De: "+ username + " A:" + toUser+ " Archivo: " + nameFile + " Path: "+ currentPath);
            }
        }
    };   
}
function editFile(){
   alert("Entro al move");
   
   var tbody = document.getElementById('file-system'); 
   var rowLength = tbody.rows.length;
   alert("Mo llega");
   for(var i=0; i<rowLength; i+=1){   
      alert("Entro al for");
      var row = tbody.rows[i];
      var state=document.getElementById("ck"+row.cells[2].innerHTML).checked;
      
      var file=row.cells[1].innerHTML;
        
      
    
      if(state===true){
            var UsuarioAction = {
            action: "edit",
            username: document.getElementById("menu-name").innerHTML,
            dir:document.getElementById("current-folder").innerHTML,
            oldfile: file,
            file: document.getElementById("name-file-mod").value,
            content: document.getElementById("cont-file-mod").value,
            ext: document.getElementById("ext-file-mod").value,
            
        }

        socket.send(JSON.stringify(UsuarioAction));
     }
    };
  
  
    
    alert("Sale");
    edit.style.display = "none";
}

function viewFile(){
   
   
   var tbody = document.getElementById('file-system'); 
   var rowLength = tbody.rows.length;
  
   for(var i=0; i<rowLength; i+=1){   
      alert("Entro al for");
      var row = tbody.rows[i];
      var state=document.getElementById("ck"+row.cells[2].innerHTML).checked;
      
      var file=row.cells[1].innerHTML;
        
      
    
      if(state===true){
            var UsuarioAction = {
            action: "view",
            username: document.getElementById("menu-name").innerHTML,
            dir:document.getElementById("current-folder").innerHTML,
            file: file,
            type:typeView
       
        }

        socket.send(JSON.stringify(UsuarioAction));
     }
    };
  
  
    
    alert("Sale");
    
}
function shareAll(){
    alert(typeShare);
    switch(typeShare){    
        case 0:
            alert("1 llega");
            shareFile();
            break;
        case 1:
            alert("1 llega");
            shareDirectory();
            break;
    }
}

function init() {
    
    //getUsers();
    //hideForm();
}

