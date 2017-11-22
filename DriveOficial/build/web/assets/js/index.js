
/* global False, users, prop, True, parseUri, current_user, value, key */

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
		
	 document.location.href = 'mainFolder.html';
});

$("#shareFolder-button").click(function(event){
		
	 document.location.href = 'shareFolder.html';
});

function refresh_Page(){
    alert("Si entre jeje");
    
    var mySearch =document.location.search.substring(1);
    [key, value] = mySearch.split("=");
    alert(document.getElementById("current_username").value);
    document.getElementById("current_username").value=value;
    alert("Si sali jeje");
}