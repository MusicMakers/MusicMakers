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

	$('thead').data('files',[]);
	$('#upload_files').change(function(){
		var initial_count = $('thead').data('files').length;
		var allowedFileTypes = [];
		var files = $('#upload_files')[0].files;

		for(var i =0; i<files.length; i++){
			$('thead').data('files').push(files[i]);
		};

		files = $('thead').data('files');

		for(var i = initial_count; i<files.length; i++){
			var index = $(document.createElement('td'));
			index.html(initial_count+1);
			var file_name = $(document.createElement('td'));
			file_name.html(files[i].name);
			var file_type = $(document.createElement('td'));
			file_type.html((files[i].type=="")?"unknown file type":files[i].type);
			var file_size = $(document.createElement('td'));
			file_size.html(formatBytes(files[i].size));
			var remove_btn = $(document.createElement('td')).append($(document.createElement('a')));
			remove_btn.children('a').attr({
				type:"button",
				class:"glyphicon glyphicon-remove"
			});
			var row = $(document.createElement('tr'));
			row.append(index);
			row.append(file_name);
			row.append(file_type);
			row.append(file_size);
			row.append(remove_btn);
			$('tbody').append(row);
		};
		console.log('files.length= '+$('#upload_files')[0].files.length);
		//$('#upload_files').closest('form').reset();
		console.log('files.length= '+$('#upload_files')[0].files.length);
	});

	$('button[name="i_am_done"]').click(function(){
		alert('Call java app! Not implemented yet');
	});

	$('.glyphicon-remove').click(function(){
		alert('File removed!');
	});
};

function formatBytes(bytes,decimals) {
   if(bytes == 0) return '0 Byte';
   var k = 1000; // or 1024 for binary
   var dm = decimals + 1 || 3;
   var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
   var i = Math.floor(Math.log(bytes) / Math.log(k));
   return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}

$(document).ready(main);