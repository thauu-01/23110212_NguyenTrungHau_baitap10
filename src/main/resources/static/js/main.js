$(document).ready(function() {
    // Load Categories via AJAX
    $.ajax({
        url: '/api/categories',
        type: 'GET',
        success: function(response) {
            if (response.status) {
                var html = '<ul>';
                response.body.forEach(function(cat) {
                    html += '<li>' + cat.categoryName + '</li>';
                });
                html += '</ul>';
                $('#categoryList').html(html);
            }
        }
    });

    // Load Products via AJAX
    $.ajax({
        url: '/api/products',
        type: 'GET',
        success: function(response) {
            if (response.status) {
                var html = '<ul>';
                response.body.forEach(function(prod) {
                    html += '<li>' + prod.productName + ' - ' + prod.unitPrice + '</li>';
                });
                html += '</ul>';
                $('#productList').html(html);
            }
        }
    });
});