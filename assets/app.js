var main = function(){

	$('button').click(function(event) {
		event.preventDefault(); 
	});

	$('button[name="compose_now_btn"]').click(function(){
		$('div[name="uploader"]').show();
		$('button[name="compose_now_btn"]').hide();
	});

	$('button[name="upload_btn"]').click(function(){
		$('#upload_files').click();
	});

	$('button[name="upload"]').click(function(){
		alert('Files uploaded!');
	});

	$('.glyphicon-remove').click(function(){
		alert('File removed!');
	});
};

$(document).ready(main);