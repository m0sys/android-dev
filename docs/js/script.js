$(function() {
	// Get scroll distance from top
	var scrollDistance = $(window).scrollTop();

	// Get baseurl and url from liquid-vars div 
	var baseurl = $(".liquid-vars").attr("baseurl");
	var url = $(".liquid-vars").attr("url");

	// Debugging
	console.log("Scroll Distance: " + scrollDistance);
	console.log("href = " + $(".navbar-brand").attr("href"));
	console.log("baseurl = " + baseurl);
	console.log("url = " + url);

	$(window).scroll(function() {
		// Update scroll distance from top
		scrollDistance = $(window).scrollTop();
		console.log("Scroll Distance: " + scrollDistance);
		console.log("href = " + $(".navbar-brand").attr("href"));

		if (scrollDistance == 0) {
			$(".navbar-brand").attr("href", url);
		}
		
		else {
			$(".navbar-brand").attr("href", "#page-top");
		}
	});

	// $(".navbar-brand").click(function() {
	// 	if (scrollDistance == 0) {
	// 		$(".navbar-brand").attr("href", url);
	// 	}
	// 	
	// 	else {
	// 		$(".navbar-brand").attr("href", "#page-top");
	// 	}

	// });
})
