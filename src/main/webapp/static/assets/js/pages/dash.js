$(function () {
    $('#formLogin').bootstrapValidator({
        live: 'submitted',
        feedbackIcons: {
            valid: '',
            invalid: '',
            validating: ''
        },
        fields: {
        	j_username: {
                validators: {
                    notEmpty: {
                        message: 'E-mail is required'
                    }
                }
            },
            j_password: {
                validators: {
                    notEmpty: {
                        message: 'Password is required'
                    }
                }
            }
        }
    });
});

