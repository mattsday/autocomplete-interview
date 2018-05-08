function connect() {
	var socket = new SockJS(autocomplete + '/autocomplete');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		stompClient.subscribe('/topic/ws-autocomplete', function(response) {
			var success = pending.shift();
			success(JSON.parse(response.body));
		});
	});
}

connect();

var catalog = new Bloodhound({
	datumTokenizer : Bloodhound.tokenizers.obj.whitespace('value'),
	queryTokenizer : Bloodhound.tokenizers.whitespace,

	remote : {
        url: '/account/search#%QUERY',
        wildcard: '%QUERY',
		transport : function(opts, onSuccess, onError) {
			var string = opts.url.split("#")[1];
			stompClient.send("/app/ws-get", {}, decodeURIComponent(string));
			pending.push(onSuccess);
		}
	}
});
$('#remote .typeahead').typeahead(null, {
	name : 'catalog',
	source : catalog
});

// Queue to handle websocket requests
var pending = [];

// submit the form on selecting an option
jQuery('#remote').on('typeahead:selected', function(e, datum) {
	$("#search").submit();
});
$(document).ready(function() {
	$("#searchBox").focus();
});