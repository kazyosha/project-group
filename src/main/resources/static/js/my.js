// my.js - Quản lý borrows & search sinh viên
console.log("✅ my.js loaded");

function initBorrowList() {
    // CSRF cho tất cả AJAX
    $(document).ajaxSend(function(e, xhr, options) {
        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");
        xhr.setRequestHeader(header, token);
    });

    let timer;

    // --- Search sinh viên cho tạo borrow ---
    $('#studentInput').off('input').on('input', function() {
        const query = $(this).val();
        clearTimeout(timer);

        if (query.length < 2) {
            $('#studentList').empty();
            return;
        }

        timer = setTimeout(() => {
            $.getJSON('/admin/borrows/customers/search', { q: query }, function(data) {
                let list = '';
                if (data.length > 0) {
                    data.forEach(s => {
                        list += `<li class="list-group-item list-group-item-action" data-id="${s.id}">${s.code} - ${s.name}</li>`;
                    });
                } else {
                    list = '<li class="list-group-item text-muted">Không tìm thấy</li>';
                }
                $('#studentList').html(list);
            });
        }, 300);
    });

    $('#studentList').off('mousedown').on('mousedown', 'li', function(e) {
        const name = $(this).text();
        const id = $(this).data('id');
        $('#studentInput').val(name);
        $('#studentId').val(id);
        $('#studentList').empty();
        e.preventDefault();
    });

    $(document).off('click.borrowList').on('click.borrowList', function(e) {
        if (!$(e.target).closest('#studentInput, #studentList').length) {
            $('#studentList').empty();
        }
    });

    // --- Search customers ---
    $("#searchBox").off('input.customers').on("input.customers", function () {
        let keyword = $(this).val();
        $.ajax({
            url: "/admin/customers/search",
            data: {keyword: keyword},
            success: function (data) {
                let rows = "";
                if (data && data.content && data.content.length > 0) {
                    data.content.forEach(c => {
                        rows += `
                        <tr>
                            <td>${c.id}</td>
                            <td>${c.code}</td>
                            <td>${c.name}</td>
                            <td>${c.schoolClass || ""}</td>
                            <td>${c.address || ""}</td>
                            <td>${c.birthDate || ""}</td>
                            <td class="text-center">
                                <a href="/admin/customers/${c.id}/edit" class="btn btn-sm btn-warning">✏️</a>
                                <a href="/admin/customers/${c.id}/delete" class="btn btn-sm btn-danger"
                                   onclick="return confirm('Bạn có chắc muốn xóa?')">🗑️</a>
                            </td>
                        </tr>`;
                    });
                } else {
                    rows = `<tr><td colspan="7" class="text-center">Không tìm thấy khách hàng</td></tr>`;
                }
                $("#customersBody").html(rows);
            },
            error: function (xhr) {
                console.error("Search error:", xhr);
            }
        });
    });

    // --- Load borrows ---
    function loadBorrows(page = 0) {
        let keyword = $("#searchBox").val();
        let status = $("#statusFilter").val();

        $.ajax({
            url: "/admin/borrows/search-list",
            data: {keyword, status, page, size: 5},
            success: function (data) {
                let rows = "";
                data.content.forEach(r => {
                    let details = r.borrowDetails.map(d => d.bookTitle).join(", ");
                    rows += `
                        <tr>
                            <td>${r.id}</td>
                            <td>${r.customerName}</td>
                            <td>${r.borrowDate || ""}</td>
                            <td>${r.status}</td>
                            <td>${details}</td>
                            <td>
                                <form class="statusForm" data-id="${r.id}">
                                    <select name="status" class="form-select form-select-sm">
                                        <option value="BORROWING" ${r.status === 'BORROWING' ? 'selected' : ''}>Đang mượn</option>
                                        <option value="OVERDUE" ${r.status === 'OVERDUE' ? 'selected' : ''}>Quá hạn</option>
                                        <option value="RETURNED" ${r.status === 'RETURNED' ? 'selected' : ''}>Đã trả</option>
                                    </select>
                                </form>
                            </td>
                        </tr>`;
                });
                $("#borrowBody").html(rows);

                // Pagination
                let pagination = "";
                if (data.totalPages > 1) {
                    if (data.number > 0) {
                        pagination += `<li class="page-item"><a class="page-link" href="#" data-page="${data.number-1}">«</a></li>`;
                    }
                    for (let i = 0; i < data.totalPages; i++) {
                        pagination += `<li class="page-item ${i === data.number ? 'active' : ''}">
                            <a class="page-link" href="#" data-page="${i}">${i+1}</a></li>`;
                    }
                    if (!data.last) {
                        pagination += `<li class="page-item"><a class="page-link" href="#" data-page="${data.number+1}">»</a></li>`;
                    }
                }
                $("#pagination").html(pagination);
            }
        });
    }

    // Gọi lần đầu
    loadBorrows();

    // Search & filter & pagination
    $("#searchBox").off('input.borrows').on("input.borrows", () => loadBorrows(0));
    $("#statusFilter").off('change.borrows').on("change.borrows", () => loadBorrows(0));
    $("#pagination").off('click.borrows').on("click.borrows", "a", function(e) {
        e.preventDefault();
        let page = $(this).data("page");
        loadBorrows(page);
    });

    // Update status
    $("#borrowBody").off('change.status').on("change.status", "select[name='status']", function() {
        const form = $(this).closest(".statusForm");
        const borrowId = form.data("id");
        const status = $(this).val();
        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: `/admin/borrows/${borrowId}/status`,
            type: "POST",
            data: { status },
            beforeSend: xhr => xhr.setRequestHeader(header, token),
            success: () => form.closest("tr").find("td:eq(3)").text(status),
            error: xhr => console.error("Lỗi khi cập nhật trạng thái:", xhr)
        });
    });
}

// Khi reload.js load xong hoặc AJAX load nội dung mới
if (typeof initBorrowList === "function") initBorrowList();
