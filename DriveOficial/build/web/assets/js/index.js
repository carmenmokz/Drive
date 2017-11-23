
/* global False, users, prop, True, parseUri, current_user, value, key, btnDeleteFiles, btnDeleteFolders */

function refreshFiles(json){
    
    var files=json.archivos;
   
    
    for (i in files){
           var tr1 = document.createElement("tr");
           var td1= document.createElement("td");

           td1.innerHTML="<i class=\"pe-7s-file\"></i>";
           var td2= document.createElement("td");
           td2.innerHTML=files[i].name+"."+files[i].ext;
           var td4= document.createElement("td");
           td4.innerHTML=files[i].name;
           var td5= document.createElement("td");
           td5.innerHTML=files[i].name;
           var td6= document.createElement("td");
           td6.innerHTML="<div class=\"checkbox\"><input id=\"ck"+files[i].name+"\" type=\"checkbox\"><label for=\"ck"+files[i].name+"\"></label></div>";
           tr1.appendChild(td1);
           tr1.appendChild(td2);
           tr1.appendChild(td4);
           tr1.appendChild(td5);
           tr1.appendChild(td6);
           document.getElementById("file-system").appendChild(tr1);
    
    }
                                        
}
function refreshFolders(json){
     $("#file-system").empty();
    var folders=json.folders;

    for (i in folders){
           var tr1 = document.createElement("tr");
           
           
           
           
           var td1= document.createElement("td");
           td1.innerHTML="<i class=\"pe-7s-folder\" ></i>";
           td1.id="/"+folders[i].name;
           td1.setAttribute("onclick","changeFolder(this.id)");
           var td2= document.createElement("td");
           td2.innerHTML="/"+folders[i].name;
           var td4= document.createElement("td");
           td4.innerHTML=folders[i].name;
           var td5= document.createElement("td");
           td5.innerHTML=folders[i].name;
           var td6= document.createElement("td");
           td6.innerHTML="<div class=\"checkbox\"><input id=\"ck"+folders[i].name+"\" type=\"checkbox\"><label for=\"ck"+folders[i].name+"\"></label></div>";
           tr1.appendChild(td1);
           tr1.appendChild(td2);
           tr1.appendChild(td4);
           tr1.appendChild(td5);
           tr1.appendChild(td6);
           document.getElementById("file-system").appendChild(tr1);
    
    }
                                        
}
function refreshFolder(usuario){
    alert("si me llamaron");
    document.getElementById("current-folder").innerHTML=usuario.dir;
}
$("#login-button").click(function(event){
        getUsers();
	event.preventDefault();
         var user=document.getElementById("user-index").value;
         var pass=document.getElementById("pass-index").value;
         var userC=users;
         var find=0;
         var prop;
         for (prop in userC) {
            if(user.localeCompare(userC[prop].username)===0 && pass.localeCompare(userC[prop].pass)===0){
                document.location.href = 'initPage.html?current_user='+user;
          

                find=1;
                break;
            }
         
        }
        if(find===0){
            alert("¡Usuario o Contraseña Incorreto!")
        }
         
         
});

 $("#mainFolder-button").click(function(event){
		
	 document.location.href = 'mainFolder.html?current_user='+document.getElementById("menu-name").innerHTML;
});

$("#shareFolder-button").click(function(event){
		
	 document.location.href = 'shareFolder.html?current_user='+document.getElementById("menu-name").innerHTML;
});
$("#initPage").click(function(event){
		
	 document.location.href = 'initPage.html?current_user='+document.getElementById("menu-name").innerHTML;
});
$("#shareFolder").click(function(event){
		
	 document.location.href = 'shareFolder.html?current_user='+document.getElementById("menu-name").innerHTML;
});
$("#mainFolder").click(function(event){
		
	 document.location.href = 'mainFolder.html?current_user='+document.getElementById("menu-name").innerHTML;
});

function refresh_Page(){
    var mySearch =document.location.search.substring(1);
    [key, value] = mySearch.split("=");
    document.getElementById("current-username").innerHTML=value;
    document.getElementById("menu-name").innerHTML=value;
    document.getElementById("created-by").innerHTML=value;
    
    demo.initChartist();
}
function refresh_PageMainF(){
    var mySearch =document.location.search.substring(1);
    [key, value] = mySearch.split("=");
    document.getElementById("menu-name").innerHTML=value;
    document.getElementById("created-by").innerHTML=value;
    demo.initChartist();
}
function refresh_PageShareF(){
    var mySearch =document.location.search.substring(1);
    [key, value] = mySearch.split("=");
    document.getElementById("menu-name").innerHTML=value;
    demo.initChartist();
}


// Get the modal
var addFileDisp = document.getElementById('addFile');
var addFolderDisp = document.getElementById('addFolder');
var copy = document.getElementById('copyWindow');
var move = document.getElementById('moveWindow');
var share=document.getElementById('shareWindow');
var edit=document.getElementById('editWindow');
var info=document.getElementById('infoWindow');
// Get the button that opens the modal
var btnFile = document.getElementById("btnAddFile");
var btnFolder = document.getElementById("btnAddFolder");
var btnCopyVR = document.getElementById("btnCopyVR");
var btnCopyRV = document.getElementById("btnCopyRV");
var btnCopyVV = document.getElementById("btnCopyVV");
var btnDelete= document.getElementById("btnDelete");
// Get the <span> element that closes the modal
var typeCopy=0;
var typeMove=0;
var typeShare=0;
// When the user clicks the button, open the modal 
btnFile.onclick = function() {
    addFileDisp.style.display = "block";
};
btnFolder.onclick = function() {
    addFolderDisp.style.display = "block";
};
btnCopyVR.onclick = function() {
    copy.style.display = "block";
    typeCopy=0;
};
btnCopyRV.onclick = function() {
    copy.style.display = "block";
    typeCopy=1;
};
btnCopyVV.onclick = function() {
    copy.style.display = "block";
    typeCopy=2;
};
btnDeleteFiles.onclick = function() {
   alert("Ya funco");
   deleteFile();
};
btnDeleteFolders.onclick = function() {
   alert("Ya funco");
   deleteFolder();
};
btnMoveFiles.onclick = function() {
   move.style.display = "block";
   typeMove=0;
};
btnMoveFolders.onclick = function() {
  move.style.display = "block";  
  typeMove=1;
};
btnShareFiles.onclick = function() {
   share.style.display = "block";
   typeShare=0;
};
btnShareFolders.onclick = function() {
  share.style.display = "block";  
  typeShare=1;
};
btnViewFiles.onclick = function() {
  info.style.display = "block";  
 
};
btnEditFiles.onclick = function() {
  edit.style.display = "block";  

};
window.onclick = function(event) {
     if (event.target === addFileDisp) {
         addFileDisp.style.display = "none";
     }
     else if (event.target === addFolderDisp) {
         addFolderDisp.style.display = "none";
     }
     else if (event.target === copy) {
         copy.style.display = "none";
     }
     else if (event.target === move) {
         move.style.display = "none";
     }
     else if (event.target === share) {
         share.style.display = "none";
     }
     else if (event.target === info) {
         info.style.display = "none";
     }
     else if (event.target === edit) {
         edit.style.display = "none";
     }
 };
// When the user clicks on <span> (x), close the modal






