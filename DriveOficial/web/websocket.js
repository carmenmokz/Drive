window.onload = init;
var socket = new WebSocket("ws://localhost:8080/DriveOficial/actions");
socket.onmessage = onMessage;
var users;
function onMessage(event) {
    var usuario = JSON.parse(event.data);
    if (usuario.action === "add") {
        getUsers();
    }
   else  if (usuario.action === "remove") {
        //document.getElementById(device.id).remove();
        //device.parentNode.removeChild(device);
    }
    else if (usuario.action === "getUsers") {
        users = usuario.users;
    }
   else  if (usuario.action === "getMainFolder") {

       refreshFolders(usuario);
       refreshFiles(usuario);
       
  
    }
    
   else  if (usuario.action === "getShareFolder") {
       refreshFolders(usuario);
       refreshFiles(usuario);
     
    }
   else  if (usuario.action === "changeFolder") {
      
       refreshFolder(usuario);
       refreshFolders(usuario);
       refreshFiles(usuario);
    }
    else if (usuario.action === "view") {
      
      
       refreshView(usuario);
    }
    else if(usuario.action==="verFolder"){
        verFolder(usuario);

    }
    else if(usuario.action==="verFile"){
        verFile(usuario);

    }
     else if(usuario.action==="verTodos"){
        verTodos(usuario);

    }
    else if(usuario.action==="NoDisponible"){
        alert("La cantidad de memoria disponible no permite hacer la accion que desea ):");
    }


}
function verTodos(usuario){
    if (confirm('El directorio o archivo ya existe. ¿Desea caerle encima?')) {
       if(usuario.c===1){
        var fileA=usuario.origin.split("/");
    }else{
         var fileA=usuario.origin.split("\\");
    }

        var [del,ext]=fileA[fileA.length-1].split(".");
        var UsuarioAction = {
            action: "deleteFile",
            username: document.getElementById("menu-name").innerHTML,
            file: del,
            dir: usuario.destiny,
            ext: ext
        };
        socket.send(JSON.stringify(UsuarioAction));
         var UsuarioAction = {
           action: "deleteFolder",
           username: document.getElementById("menu-name").innerHTML,
           file: fileA[fileA.length-1],
           dir: usuario.destiny

        };
        socket.send(JSON.stringify(UsuarioAction));
   
        var UsuarioAction = {
           action: "copy",
           username: document.getElementById("menu-name").innerHTML,
           origin: usuario.origin,
           destiny: usuario.destiny,
           type: usuario.typeCopy
        };
        socket.send(JSON.stringify(UsuarioAction));
    }else{

    }
             
}
function verFolder(usuario){
    if (confirm('El directorio ya existe. ¿Desea caerle encima?')) {
       
        switch(usuario.sol){
            case 0:
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
                break;
            
            case 2:
               
                var fileA=usuario.file.split("/");
              
               
               
                var UsuarioAction = {
                    action: "deleteFolder",
                    username: document.getElementById("menu-name").innerHTML,
                    file: fileA[fileA.length-1],
                    dir: usuario.destiny

                };
                socket.send(JSON.stringify(UsuarioAction));

                var UsuarioAction = {
                    action: "move",
                    username: document.getElementById("menu-name").innerHTML,
                    dir:document.getElementById("current-folder").innerHTML,
                    file: fileA[fileA.length-1],
                    destiny: usuario.destiny,
                    type: usuario.typeMove
                };
                
                socket.send(JSON.stringify(UsuarioAction));
                
                break;
            case 3:
                var fileA=usuario.directory.split("/");
                var UsuarioAction = {
                    action: "deleteFolder",
                    username: usuario.toUser,
                    file: fileA[fileA.length-1],
                    dir: "D/Compartido"

                };
                socket.send(JSON.stringify(UsuarioAction));
                var UsuarioAction = {
                    action: "shareDir",
                    username: document.getElementById("menu-name").innerHTML,
                    path: usuario.directory, 
                    toUser: usuario.toUser

                };
                socket.send(JSON.stringify(UsuarioAction));
                document.location.href = 'mainFolder.html?current_user='+document.getElementById("menu-name").innerHTML;
                break;    
            
        }
       
    }    else {
           
        }
}
function verFile(usuario){
    if (confirm('El archivo ya existe. ¿Desea caerle encima?')) {
        switch(usuario.sol){
            case 0:
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
                    break;
            case 2:
                var fileA=usuario.file.split("/");
                var [name,ext]=fileA[fileA.length-1].split(".");
              
                var UsuarioAction = {
                        action: "deleteFile",
                        username: document.getElementById("menu-name").innerHTML,
                        file:name,
                        dir: usuario.destiny,
                        ext: ext
                };
                socket.send(JSON.stringify(UsuarioAction));
           
                var UsuarioAction = {
                        action: "move",
                        username: document.getElementById("menu-name").innerHTML,
                        dir:document.getElementById("current-folder").innerHTML,
                        file: fileA[fileA.length-1],
                        destiny: usuario.destiny,
                        type: usuario.typeMove
                 };
                socket.send(JSON.stringify(UsuarioAction));
                break;
            case 3:
                var fileA=usuario.file.split("/");
                var [name,ext]=usuario.fileA.split(".");
                var UsuarioAction = {
                    action: "deleteFile",
                    username: usuario.toUser,
                    file:name,
                    dir: "D/Compartido",
                    ext: ext

                };
                socket.send(JSON.stringify(UsuarioAction));
                var UsuarioAction = {
                    action: "shareFile",
                    username: document.getElementById("menu-name").innerHTML,
                    file: usuario.file, 
                    currentPath: usuario.currentPath, 
                    toUser: usuario.toUser

                };
                socket.send(JSON.stringify(UsuarioAction));
                document.location.href = 'mainFolder.html?current_user='+document.getElementById("menu-name").innerHTML;
                break;    
                    
                 
            }
    } else {
            
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
    share.style.display = "none";
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
    
    
   
    var UsuarioAction = {
        action: "copy",
        username: document.getElementById("menu-name").innerHTML,
        origin: origin,
        destiny: destiny,
        type: typeCopy
    };
   
    socket.send(JSON.stringify(UsuarioAction));
    copy.style.display = "none";
}
function deleteFile(){
    var tbody = document.getElementById('file-system');
    var rowLength = tbody.rows.length;

    for(var i=0; i<rowLength; i+=1){   
      var row = tbody.rows[i];
      var state=document.getElementById("ck"+row.cells[2].innerHTML).checked;
      
      if(state===true){
        [file,ext]=row.cells[1].innerHTML.split(".");
     
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
 
   var destiny =document.getElementById("destiny-move").value;
   var tbody = document.getElementById('file-system'); 
   var rowLength = tbody.rows.length;
   
   for(var i=0; i<rowLength; i+=1){   
    
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
    };
    socket.send(JSON.stringify(UsuarioAction));
    }
     
    };
  
  
    

    move.style.display = "none";
}

function shareDirectory(){
 
    var username = document.getElementById("menu-name").innerHTML; 
 
    var toUser = document.getElementById("user-share").value; 
    var path = document.getElementById("current-folder").innerHTML; 

    var tbody = document.getElementById('file-system'); 
    var rowLength = tbody.rows.length;

    for(var i=0; i<rowLength; i+=1){   
    
    var row = tbody.rows[i];
    var state=document.getElementById("ck"+row.cells[2].innerHTML).checked;
      
    if(state===true){
       
      
        var file=row.cells[2].innerHTML;
        
        var finalpath=path+"/"+file;
  
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
  
    var username = document.getElementById("menu-name").innerHTML; 

     
    
    var currentPath = document.getElementById("current-folder").innerHTML; 

    var toUser = document.getElementById("user-share").value; 

    var tbody = document.getElementById('file-system'); 
    
    var rowLength = tbody.rows.length;

    for(var i=0; i<rowLength; i+=1){   
        
        var row = tbody.rows[i];
       
        var state= document.getElementById("ck"+row.cells[2].innerHTML).checked;

        if(state===true){
            var file = row.cells[1].innerHTML;
             
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
  
   
   var tbody = document.getElementById('file-system'); 
   var rowLength = tbody.rows.length;

   for(var i=0; i<rowLength; i+=1){   

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
  
  

    edit.style.display = "none";
}

function viewFile(){
   
   
   var tbody = document.getElementById('file-system'); 
   var rowLength = tbody.rows.length;
  
   for(var i=0; i<rowLength; i+=1){   
     
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
  
  
    
    
    
}
function shareAll(){
   
    switch(typeShare){    
        case 0:
            
            shareFile();
            break;
        case 1:
          
            shareDirectory();
            break;
    }
    share.style.display = "none";
}

function init() {
    
    //getUsers();
    //hideForm();
}

