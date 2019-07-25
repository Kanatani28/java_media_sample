$(function() {
	marked.setOptions({
	  sanitize: true,
	});
	
	const prevElm = $("#preview")
	$("#imageFile").on("change", function() {
		var file = $(this).prop('files')[0]
		fileRdr = new FileReader();
		
		fileRdr.onload = function() {
			prevElm.attr('src', fileRdr.result);
			prevElm.attr('width', '75%');
			
			$('#image-form').append($("<input>", {
				"value": fileRdr.result,
				"name": "data",
				type: "hidden"
			}));
			$("#image-form").append($("#imageFile"))
			var form = $('#image-form')[0];
			   
		    // FormData オブジェクトを作成
		    var formData = new FormData( form );
		    formData.append("ddd", fileRdr.result)
			$.ajax({
				url: '/images/add-image',
			    method: 'post',
		        dataType: 'text',
			    data: formData,
			    processData: false,
			    contentType: false
			}).done(function(data) {
				$("#article-body").text($("#article-body").text() + "\n\n" + "![a](" + data + ")")
			}).fail(function(XMLHttpRequest, textStatus, errorThrown){
			    console.log(XMLHttpRequest.status);
			    console.log(textStatus);
			    console.log(errorThrown);
			})
			
		}
		fileRdr.readAsDataURL(file);
	});
	
	$(".form-tab").on("click", function() {
		$(".form-tab").removeClass("active");
		$(this).addClass("active");
		const id = $(this).attr("id");
		switch (id) {
		case "tab-edit":
			$("#edit-area").show();
			$("#preview-area").hide();
			break;

		case "tab-preview":
			$("#edit-area").hide();
			$("#article-title-preview").text($("#article-title").val());
			$("#article-body-preview").html(marked($("#article-body").val()));
			$("#preview-area").show();
			break;

		default:
			break;
		}
	});
})