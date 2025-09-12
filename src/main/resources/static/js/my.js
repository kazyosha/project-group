$(document).ready(function() {
    $('#studentInput').on('input', function() {
        const query = $(this).val();
        if (query.length < 2) { $('#studentList').empty(); return; }
        $.getJSON('/admin/students/search', { q: query }, function(data) {
            let list = '';
            data.forEach(s => {
                list += `<li class="list-group-item list-group-item-action" data-id="${s.id}">${s.code} - ${s.name}</li>`;
            });
            $('#studentList').html(list);
        });
    });

    $('#studentList').on('click', 'li', function() {
        const name = $(this).text();
        const id = $(this).data('id');
        $('#studentInput').val(name);
        $('#studentId').val(id);
        $('#studentList').empty();
    });

    // Ẩn danh sách khi click ngoài
    $(document).click(function(e) {
        if (!$(e.target).closest('#studentInput, #studentList').length) {
            $('#studentList').empty();
        }
    });
});