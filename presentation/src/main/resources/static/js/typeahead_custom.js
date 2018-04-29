var catalog = new Bloodhound({
	datumTokenizer : Bloodhound.tokenizers.obj.whitespace('value'),
	queryTokenizer : Bloodhound.tokenizers.whitespace,
	remote : {
		url : autocomplete + '/get?q=%QUERY',
		wildcard : '%QUERY',
	}
});
$('#remote .typeahead').typeahead(null, {
	name : 'catalog',
	source : catalog
});

// submit the form on selecting an option
jQuery('#remote').on('typeahead:selected', function(e, datum) {
	$("#search").submit();
});
