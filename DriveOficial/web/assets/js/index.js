 $("#login-button").click(function(event){
	event.preventDefault();
	

         var user=document.getElementById("user-index").value;
         var pass=document.getElementById("pass-index").value;
        
         if(user.localeCompare("admin")===0 && pass.localeCompare("1234")===0){
             document.location.href = 'initPage.html';
         }else{
             alert("No existe ese usuario.");
         }
         
});

 $("#mainFolder-button").click(function(event){
		
	 document.location.href = 'mainFolder.html';
});

$("#shareFolder-button").click(function(event){
		
	 document.location.href = 'shareFolder.html';
});