  var callerId = 'undefined';

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

function goBack() {
  window.history.go(-1);
}

function modifyPart() {
  var singleParam;
  var actionParam;
  var partProject;
  var urlParams = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
  for (var i = 0; i < urlParams.length; i++) {
    singleParam = urlParams[i].split('=');
    if (singleParam[0] == 'q') {
      partProject = singleParam[1];
    } else if (singleParam[0] == 'p') {
      actionParam = singleParam[1];
      i = urlParams.length;
    }
  }
  modify();
  if(actionParam == 'add') {
    $('.noActivateOnAdd').prop('readonly', true);
  }
  var icoAdd = "web/img/ico-add.png";
  $('.addElement').attr('src', icoAdd);
  if (partProject == 'act') {
    $('#act-addNote').attr('href', '#note-div').attr('rel', 'modal:open');
    $('#act-addResult').attr('href', '#result-div').attr('rel', 'modal:open');
  } else if (partProject == 'wbs') {
    $('#wbs-addNote').attr('href', '#note-div').attr('rel', 'modal:open');
    $('#wbs-addResult').attr('href', '#result-div').attr('rel', 'modal:open');
  } else if (partProject == 'pol') {
    $('#add-all').attr('href', '#note-form').attr('rel', 'modal:open');
    $('#add-label').wrap('<a id="label-for" />');
    $('#label-for').attr('href', '#note-form').attr('rel', 'modal:open');
  } else if (partProject == 'mon') {
    $('#d4-all').attr('href', '#note-form').attr('rel', 'modal:open');
    $('#d4-label').wrap('<a id="label-for-d4" />');
    $('#label-for-d4').attr('href', '#note-form').attr('rel', 'modal:open').attr('title', 'Clicca per aggiungere un allegato al quadro D.4');
    $('#d5-all').attr('href', '#note-form').attr('rel', 'modal:open');
    $('#d5-label').wrap('<a id="label-for-d5" />');
    $('#label-for-d5').attr('href', '#note-form').attr('rel', 'modal:open').attr('title', 'Clicca per aggiungere un allegato al quadro D.5');
    $('#d6-all').attr('href', '#note-form').attr('rel', 'modal:open');
    $('#d6-label').wrap('<a id="label-for-d6" />');
    $('#label-for-d6').attr('href', '#note-form').attr('rel', 'modal:open').attr('title', 'Clicca per aggiungere un allegato al quadro D.6');
    $('#d7-all').attr('href', '#note-form').attr('rel', 'modal:open');
    $('#d7-label').wrap('<a id="label-for-d7" />');
    $('#label-for-d7').attr('href', '#note-form').attr('rel', 'modal:open').attr('title', 'Clicca per aggiungere un allegato al quadro D.7');
    $('#d8-all').attr('href', '#note-form').attr('rel', 'modal:open');
    $('#d8-label').wrap('<a id="label-for-d8" />');
    $('#label-for-d8').attr('href', '#note-form').attr('rel', 'modal:open').attr('title', 'Clicca per aggiungere un allegato al quadro D.8');
  }
}

function modify() {
  $('textarea').prop('readonly', false);
  $('input[type=\'text\']').prop('readonly', false);
  $('input[type=\'checkbox\']').prop('disabled', false);
  $('select').prop('disabled', false);
  $('button').prop('disabled', false);
  $('input[type=\'text\'].calendarData').datepicker();
  $('input[type=\'submit\']').prop('disabled', false);
  $('img').prop('disabled', false);
  $('.calendarData').on('click', function(e) {
    e.preventDefault();
    $('.calendarData').attr("autocomplete", "off");  
  });
  $('#btnMod').prop('disabled', true);
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
      return new Date(yearFinalDate, monthFinalDate - 1, dayFinalDate) > new Date(yearFirstDate, monthFirstDate - 1, dayFirstDate);
    }
    return isNaN(value) && isNaN($(params).val()) 
        || (Number(value) > Number($(params).val()));
  }, 'Must be greater than {0}.'
);

jQuery.validator.addMethod("lessThan", 
  function (value, element, params) {
    var finalDate = value.split('/');
    var dayFinalDate = finalDate[0];
    var monthFinalDate = finalDate[1];
    var yearFinalDate = finalDate[2];
    var runtimeDate = new Date(yearFinalDate, monthFinalDate - 1, dayFinalDate);
    var rightNow = new Date();
    var dayRuntimeDate = runtimeDate.getUTCDate() + 1;
    var monthRuntimeDate = runtimeDate.getUTCMonth();
    var yearRuntimeDate = runtimeDate.getUTCFullYear();
    var dayCurrentDate = rightNow.getUTCDate();
    var monthCurrentDate = rightNow.getUTCMonth();
    var yearCurrentDate = rightNow.getUTCFullYear();
    var runtimeDateAsMilliseconds = Date.UTC(yearRuntimeDate, monthRuntimeDate, dayRuntimeDate, 0, 0, 0);
    var tomorrowDateAsMilliseconds = Date.UTC(yearCurrentDate, monthCurrentDate, dayCurrentDate + 1, 0, 0, 0);
    if (!/Invalid|NaN/.test(runtimeDate)) {
      if (runtimeDateAsMilliseconds < tomorrowDateAsMilliseconds){
        return true;
      }
      return false;
    }
    return true;
  }, 'Must be less than {0}.'
);

function selectionEdit(element) {
  if(!$("input[type='radio']").is(":checked")) {
    alert("E' necessario selezionare una " + element + " da modificare!");
    return false;
    //$('<div></div>').html("E' necessario selezionare una " + element + " da modificare!").dialog({
    //  title: "Attenzione!!!",
    //  resizable: false,
    //  buttons: {
    //    'Ok': function()  {
    //        $( this ).dialog( 'close' );
    //    }
    //  }
    //});
  };
}

function selectionDelete(msg) {
  if(!$("input[type='radio']").is(":checked")) {
    alert(msg);
    return false;
  };
}

function submitForm() {
  $('form').submit();
}

function confirmSubmit(type) {
  if (type == 'wbs') {
    if ($("input[name='wbs-select']:checked").length) {
      var $radioValue = $("input[name='wbs-select']:checked").val();
      return confirm("Sei sicuro di voler eliminare la " + type + " " + $radioValue + "?");
    }
    alert("Devi selezionare una " + type + " da eliminare");
    return false;
  } else {
    if ($("input[name='act-select']:checked").length) {
      var $radioValue = $("input[name='act-select']:checked").val();
      return confirm("Sei sicuro di voler eliminare la " + type + " " + $radioValue + "?");
    }
    alert("Devi selezionare una " + type + " da eliminare");
    return false;
  }
}

function showActivities(indirizzo) {
  alert('Occorre selezionare una WBS per visualizzarne le attivita\'');
  window.location.href = indirizzo;
}

// --------------------------  POP-UP ( incarichi)---------------
// Trap Mouse Position 
//Copyright 2006,2007 Bontrager Connection, LLC
  var cX = 0;var cY = 0;var rX = 0;var rY = 0;
  
  function UpdateCursorPosition(e){
    cX = e.pageX; cY = e.pageY;
  }
  
  function UpdateCursorPositionDocAll(e){
    cX = event.clientX; cY = event.clientY;
  }
  
  if(document.all) {
    document.onmousemove = UpdateCursorPositionDocAll;
  } else {
    document.onmousemove = UpdateCursorPosition;
  }
  
  function AssignPosition(d, tipo) {
    if(self.pageYOffset) {
      rX = self.pageXOffset; rY = self.pageYOffset;
      }
    else if(document.documentElement && document.documentElement.scrollTop) {
      rX = document.documentElement.scrollLeft; rY = document.documentElement.scrollTop;
      }
    else if(document.body) {
      rX = document.body.scrollLeft; rY = document.body.scrollTop;
      }
    if(document.all) {
      cX += rX; cY += rY;
      }
    if (tipo == "Note") {
      // Sposto + a sinistra, in quanto verrebbe in parte nascosto a destra
      d.style.left = (cX-100) + "px"; d.style.top = (cY+10) + "px";
    } else {
      d.style.left = (cX+10) + "px"; d.style.top = (cY+10) + "px";
    }
  }
  
  function HideContent(d) {
    if(d.length < 1) { return; }
    document.getElementById(d).style.display = "none";
  }
  
  function ShowContent(d, tipo) {
    if(d.length < 1) { return; }
    var dd = document.getElementById(d);
    AssignPosition(dd, tipo);
    dd.style.display = "block";
    dd.style.height = "200px";
  }
  
  function ReverseContentDisplay(d) {
    if(d.length < 1) { return; }
    var dd = document.getElementById(d);
    AssignPosition(dd);
    if(dd.style.display == "none") { dd.style.display = "block"; }
    else { dd.style.display = "none"; }
  }
  //\Trap Mouse Position ------------------------------------------------
  
  function testClickPopup(nome) {
    // Chiusura
    var myname = nome.parentElement.name;
    if (!(myname == undefined)) {
      if (!(myname.indexOf('popup1')>=0)) {
        HideContent('popup1');
      }
    } else {
      var myname = nome.name;
      if (!(myname == undefined)) {
        if (!(myname.indexOf('popup1')>=0)) {
          HideContent('popup1');
        }
      } else {
        // Nasconde a prescindere
        HideContent('popup1');
      }
    }
  }

  function popupWindow(tit,o,d,t) {
      // o - Object to display.
      // d - Display, true =  display, false = hide
      // t - Text to display in the popup
      var obj = document.getElementById(o);
      var iltitolo = document.getElementById("titolopopup1");
      var contenuto = document.getElementById("popup1Text");
      
      if(d) {
          /*
          obj.style.display = 'block';
          obj.style.visibility = 'visible';
          iltitolo.innerHTML = tit;
          contenuto.innerHTML = t;
          obj.style.width= "350px";
          obj.style.height = "200px";
          obj.style.left = ((screen.availWidth - 700)/2);
          obj.style.top = ((screen.availHeight - 400)/2);
          */
          ShowContent(obj.id,tit);
          iltitolo.innerHTML = tit;
          contenuto.innerHTML = unescape(t);
      } else {
          /*
          contenuto.innerHTML = '';
          obj.style.display = 'none';
          obj.style.visibility = 'hidden';
          */
          contenuto.innerHTML = "";
          HideContent(obj.id);
        }
    }
