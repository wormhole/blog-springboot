$(function () {
    $('.btn-item').click(function () {
        if ($('#item').css('display') === 'none') {
            $('#item').css('display', '');
            $('#item').removeClass('hidden');
        } else {
            $('#item').css('display', 'none');
            $('#item').addClass('hidden');
        }
    });
});