var scripts = [
	"js/jquery.min.js",
	"js/materialize.1.min.js",
	"js/materialize.2.min.js",
	"js/lang.min.js",
	"js/device.min.js",
	"js/init.min.js"
];
window.onload = function(){
	var i = 0;
	var date = new Date();
	var url = scripts[i] + "?t=" + date.getTime();
	loadScript(url);
	function loadScript(url){
		var s = document.createElement('script');
		s.type = "text/javascript";
		s.src = url; 
		document.body.appendChild(s);  
		s.addEventListener("load", function(){
			i++;
			var url = scripts[i];
			if(url != undefined){
				var date = new Date();
				url +=  "?t=" + date.getTime();
				setTimeout(function(){
					loadScript(url);  
				}, 0);
			}
		});
	};
};
