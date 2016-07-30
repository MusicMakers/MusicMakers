var main = function(){

	$('button').click(function(event) {
		event.preventDefault(); 
	});

	$('button[name="compose_now_btn"]').click(function(){
		$('div[name="uploader"]').show();
		$('button[name="compose_now_btn"]').hide();
	});

	$('button[name="upload_btn"]').click(function(){
		$("input[name='upload_files']").click();
	});

	// Using .on() instead of .click() as this allows jQuery binding to newly added dom elements
	$('tbody').on('click', '.glyphicon-remove',function(){
		alert('File removed!');
		console.log(this.closest('tr').remove());
	});

	// To store the uploaded files
	$('thead').data('files',[]);
	
	// Once done uploading load the files information up into the display list
	$("input[name='upload_files']").change(function(){
		var initial_count = $('thead').data('files').length;
		var allowedFileTypes = [];
		var files = $("input[name='upload_files']")[0].files;
		$('tbody').children().remove();
		for(var i = initial_count; i<files.length; i++){
			var index = $(document.createElement('td'));
			index.html(i+1);
			var file_name = $(document.createElement('td'));
			file_name.html(files[i].name);
			var file_type = $(document.createElement('td'));
			file_type.html((files[i].type=="")?"unknown file type":files[i].type);
			var file_size = $(document.createElement('td'));
			file_size.html(formatBytes(files[i].size));
			var row = $(document.createElement('tr'));
			row.append(index);
			row.append(file_name);
			row.append(file_type);
			row.append(file_size);
			$('tbody').append(row);
		};
	});


	$('button[name="i_am_done"]').click(function(){
		document.cookie = "fileDownload=true; path=/";	
		var $form = $(this).closest('form');
		var form = $form[0];
		var formdata = new FormData(form);
		$.ajax({
			url: $form.attr('action'),
			type: 'POST',
			data: formdata,
			processData: false,
			contentType: false,
			success: waitforresponse
		});

		function waitforresponse(id) {
			
			$('div[name="uploader"]').hide();
			$('img[name="loading_spinner"]').show();
			provideDownloadLink(id);
		}

		function provideDownloadLink(id) {
			var file_name = id + '.mid';
			$('a[name="download_link"]').attr('href', file_name);
			$('img[name="loading_spinner"]').hide();
			$('button[name="download_btn"]').show();
		}

	});


	// Copied and pasted from http://jqueryfiledownload.apphb.com/
	$('button[name="download_btn"]').click(function(){
		$("a.fileDownloadCustomRichExperience").click();
	});
	$(document).on("click", "a.fileDownloadCustomRichExperience", function() {

		var $preparingFileModal = $("#preparing-file-modal");

		$preparingFileModal.dialog({ modal: true });

		$.fileDownload($(this).attr('href'), {
			successCallback: function(url) {

				$preparingFileModal.dialog('close');
			},
			failCallback: function(responseHtml, url) {

				$preparingFileModal.dialog('close');
				$("#error-modal").dialog({ modal: true });
			}
		});
        return false; //this is critical to stop the click event which will trigger a normal file download!
    });
	// end of Copied and pasted from http://jqueryfiledownload.apphb.com/
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