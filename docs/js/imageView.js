$(function() {
	$(".photo").click(function() {
		// Get image link
		var link = $(this).attr("src");

		// Create image viewer
		var imageViewer = $("<div></div>");
		imageViewer.addClass("imageViewer");

		// Set hover value for photo class to none
		$(".photo").hover(function() {
			$(this).css("border", "");
		});

		var photo = $("<img>");
		photo.addClass("photo");
		photo.attr('src', link);
		imageViewer.append(photo);

		// Add image view to DOM
		$(".main-content").append(imageViewer);
		imageViewer.fadeTo(500, 1);

		// Set event to close image viewer
		imageViewer.click(function() 
		{
			$(this).fadeTo(500, 0, function() 
			{
				$(this).remove();
				//
				// Set hover value for photo class to default
				$(".photo").hover(function() {
					$(this).css("border", "#159957");
				});
			});
		});
	});
})
