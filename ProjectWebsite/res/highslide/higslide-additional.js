hs.graphicsDir = 'highslide/graphics/';
hs.showCredits = false;
hs.creditsPosition = 'below';
hs.outlineType = null;
hs.dimmingOpacity = 0.5;
hs.align = 'center';
hs.registerOverlay({
	html: '<div class="closebutton" onclick="return hs.close(this)" title="Close"></div>',
	position: 'top right',
	useOnHtml: true,
	fade: 2 // fading the semi-transparent overlay looks bad in IE
});