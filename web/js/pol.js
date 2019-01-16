$.datepicker.regional['it'] = {
	closeText: "Chiudi",
	prevText: "&#x3C;Prec",
	nextText: "Succ&#x3E;",
	currentText: "Oggi",
	monthNames: [ "Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno",
			      "Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre" ],
	monthNamesShort: [ "Gen","Feb","Mar","Apr","Mag","Giu",
					   "Lug","Ago","Set","Ott","Nov","Dic" ],
	dayNames: [ "Domenica","Lunedì","Martedì","Mercoledì","Giovedì","Venerdì","Sabato" ],
	dayNamesShort: [ "Dom","Lun","Mar","Mer","Gio","Ven","Sab" ],
	dayNamesMin: [ "Do","Lu","Ma","Me","Gi","Ve","Sa" ],
	weekHeader: "Sm",
	dateFormat: "dd/mm/yy",
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: "" 
};
$.datepicker.setDefaults($.datepicker.regional['it']);
  
function modify() {
  $('textarea').prop('readonly', false);
  $('input[type=\'text\']').prop('readonly', false);
  $('input[type=\'checkbox\']').prop('disabled', false);
  $('select').prop('disabled', false);
  $('button').prop('disabled', false);
  $('input[type=\'text\'].calendarData').datepicker();
}
  
$('textarea').textareaAutoSize();

jQuery.validator.addMethod("greaterThan", 
  function (value, element, params) {
    var finalDate = value.split('/');
    var dayFinalDate = finalDate[0];
    var monthFinalDate = finalDate[1];
    var yearFinalDate = finalDate[2];
    var firstDate = $(params).val().split('/');
    var dayFirstDate = firstDate[0];
    var monthFirstDate = firstDate[1];
    var yearFirstDate = firstDate[2];
    if (!/Invalid|NaN/.test(new Date(yearFinalDate, monthFinalDate, dayFinalDate))) {
      return new Date(yearFinalDate, monthFinalDate, dayFinalDate) > new Date(yearFirstDate, monthFirstDate, dayFirstDate);
    }
    return isNaN(value) && isNaN($(params).val()) 
        || (Number(value) > Number($(params).val()));
  }, 'Must be greater than {0}.'
);

function selectionEdit(element) {
  if(!$("input[type='radio']").is(":checked")) {
    alert("E' necessario selezionare una " + element + " da modificare!");
    return false;
  };
}