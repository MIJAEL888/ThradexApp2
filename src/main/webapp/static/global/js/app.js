$msg_error_date = 'The Date/Time is required.';
$msg_error_empty = 'This field is required and cannot be empty.';
$msg_error_select = 'Please pick an option.';
$msg_error_time = 'The Time is required.';
$msg_error_remote = 'The Date/Time is required.';
$msg_error_long = 'The Date/Time is required.';

$(function () {
	FormValidation.Validator.selectCero = {
        validate: function(validator, $field, options) {
            var value = $field.val();
            if (value == 0) {
                return false;
            }
            return true;
        }
    };
	 
    var body = $('body');
    var header = $('.header');
    var wrapper = $('.wrapper');

    /* Begin Checkbox & Radio */
    $('input[type="checkbox"]').iCheck({
        checkboxClass: 'icheckbox_minimal-grey',
        increaseArea: '20%' // optional
    });
    $('input[type="radio"]').iCheck({
        radioClass: 'iradio_minimal-grey',
        increaseArea: '20%' // optional
    });
    $('.rating input[type="radio"]').iCheck('destroy');
    $('.switch input[type="checkbox"]').iCheck('destroy');
    $('.switch input[type="radio"]').iCheck('destroy');
    $('.btn-group[data-toggle="buttons"] input[type="radio"]').iCheck('destroy');
    $('.btn-group[data-toggle="buttons"] input[type="checkbox"]').iCheck('destroy');
    /* End Checkbox & Radio */

    /* Begin Tooltip */
    $("[data-toggle='tooltip'], [data-hover='tooltip']").tooltip();
    /* End Tooltip */

    /* Begin Popover */
    $("[data-toggle='popover'], [data-hover='popover']").popover();
    /* End Popover */

    /* Begin Panel Actions */
    $(".panel-default").each(function (index, element) {
        var me = $(this);
        $("> .panel-heading > .actions > a > i", me).click(function (e) {
            if ($(this).hasClass('ion-chevron-down')) {
                $(">.panel-body", me).slideUp(200);
                $(this).removeClass('ion-chevron-down').addClass('ion-chevron-up');
            }
            else if ($(this).hasClass('ion-chevron-up')) {
                $(">.panel-body", me).slideDown(200);
                $(this).removeClass('ion-chevron-up').addClass('ion-chevron-down');
            }
//            else if ($(this).hasClass('fa-cog')) {
//                //Show modal
//            }
            else if ($(this).hasClass('ion-refresh')) {
                var reload = $(this).parent().parent().parent().next();
                blockUI(reload);
                setTimeout(function () {
                    unblockUI(reload);
                }, 2000);
            }
            else if ($(this).hasClass('ion-close')) {
                me.remove();
            }
        });
    });
    /* End Panel Actions */

    /* Begin Button States */
    $('.loading-btn').click(function () {
        var btn = $(this)
        btn.button('loading')
        setTimeout(function () {
            btn.button('reset')
        }, 3000)
    });
    /* End Button States */

    /* Begin Checkbox Table All */
    $('.checkall').on('ifChecked ifUnchecked', function(event) {
        if (event.type == 'ifChecked') {
            $(this).closest('table').find('input[type=checkbox]').iCheck('check');
        } else {
            $(this).closest('table').find('input[type=checkbox]').iCheck('uncheck');
        }
    });
    /* End Checkbox Table All */

    /* Begin Checkbox Email All */
    $('.table-email .checkall').on('ifChecked ifUnchecked', function(event) {
        if (event.type == 'ifChecked') {
            $(this).closest('table').find('input[type=checkbox]').iCheck('check');
            $(this).closest('table').find('tbody tr').addClass('active');
        } else {
            $(this).closest('table').find('input[type=checkbox]').iCheck('uncheck');
            $(this).closest('table').find('tbody tr').removeClass('active');
        }
    });
    /* End Checkbox Email All */

    /* Begin Button Radio */
    $('.btn-radio a').on('click', function(){
        var sel = $(this).data('title');
        var tog = $(this).data('toggle');
        $('#'+tog).prop('value', sel);

        $('a[data-toggle="'+tog+'"]').not('[data-title="'+sel+'"]').removeClass('active').addClass('not-active');
        $('a[data-toggle="'+tog+'"][data-title="'+sel+'"]').removeClass('not-active').addClass('active');
    })
    /* End Button Radio */

   
    /* Begin List Group */
    $('.list-group > .list-group-item').click(function() {
        $('.list-group > .list-group-item').removeClass('active');
        $(this).addClass('active');
    });
    /* End List Group */

});

/* Begin Block UI */
function blockUI(para) {
    $(para).block({
        message : '<div class="loading-animator">Loading...</div>',
        css : {
            border : "none",
            padding : "2px",
            backgroundColor : "none"
        },
        overlayCSS : {
            backgroundColor : "#000",
            opacity : 0.2,
            cursor : "wait"
        }
    });
}
function unblockUI(para) {
    $(para).unblock();
}
/* End Block UI */


function submit(formName){
	$(formName).submit();
}

//pre-submit callback 
function showRequest(formData, jqForm, options) { 
    // formData is an array; here we use $.param to convert it to a string to display it 
    // but the form plugin does this for you automatically when it submits the data 
//    var queryString = $.param(formData); 
 
    // jqForm is a jQuery object encapsulating the form element.  To access the 
    // DOM element for the form do this: 
    // var formElement = jqForm[0]; 
 
//    alert('About to submit: \n\n' + queryString); 
 
    // here we could return false to prevent the form from being submitted; 
    // returning anything other than false will allow the form submit to continue 
    return true; 
} 
 
// post-submit callback 
function showResponse(responseText, statusText, xhr, $form)  { 
    // for normal html responses, the first argument to the success callback 
    // is the XMLHttpRequest object's responseText property 
 
    // if the ajaxSubmit method was passed an Options Object with the dataType 
    // property set to 'xml' then the first argument to the success callback 
    // is the XMLHttpRequest object's responseXML property 
 
    // if the ajaxSubmit method was passed an Options Object with the dataType 
    // property set to 'json' then the first argument to the success callback 
    // is the json data object returned by the server 
 
//    alert('status: ' + statusText + '\n\nresponseText: \n' + responseText + 
//        '\n\nThe output div should have already been updated with the responseText.'); 
} 




