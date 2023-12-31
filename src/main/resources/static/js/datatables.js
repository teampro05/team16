// 그냥 사용할 데이터 테이블 -> 세훈님
/*
$(document).ready( function () {
    let $table = $('#myTable').DataTable({
    //search 창 오른쪽 상단으로 이동
    "dom": '<"top"i>rt<"bottom"flp><"clear">',

    pageLength : 10,
    order: [[0, 'desc']], // 0번째 컬럼을 기준으로 내림차순 정렬
    info: false,
    lengthChange: false, // show entries 제거
    language: {
    emptyTable: '등록된 글, 데이터(이)가 없습니다.',
    paginate: { "next": ">", "previous": "<" } // 글자를 기호로 사용
    }
    });

$('.dataTables_paginate').css({
    'textAlign':'center',
    'float': 'none',
    'margin-top':'10px',
});

$('.dataTables_filter').remove();  // dataTable 자체 search input 없애기

$('.select_filter').change(function () { // select 선택값에 따라  해당 선택 열 input이 검색하는곳 변경
        $table.columns('').search('').draw();
        $table.columns(Number($('.select_filter').val())).search($('.search_filter').val()).draw();
    });

$('.search_filter').keyup(function () { //input의 값대로 search
        let $value = $(this).val();
        $table.columns(Number($('.select_filter').val())).search($value).draw();
    })
});
*/

// 수정한 코드 확인 해주셔요,,,!!!!

$(document).ready(function () {
    // DataTable이 이미 초기화되었는지 확인
    if ($.fn.DataTable.isDataTable('#myTable')) {
        // 기존 DataTable 제거
        $('#myTable').DataTable().destroy();
    }

    let $table = $('#myTable').DataTable({
        "dom": '<"top"i>rt<"bottom"flp><"clear">',
        pageLength: 10,
        order: [[0, 'desc']],
        info: false,
        lengthChange: false,
        language: {
            emptyTable: '등록된 글, 데이터(이)가 없습니다.',
            paginate: { "next": ">", "previous": "<" }
        }
    });

    $('.dataTables_paginate').css({
        'textAlign': 'center',
        'float': 'none',
        'margin-top': '10px',
    });

    $('.dataTables_filter').remove();

    $('.select_filter').change(function () { // select 선택값에 따라  해당 선택 열 input이 검색하는곳 변경
        $table.columns('').search('').draw();
        $table.columns(Number($('.select_filter').val())).search($('.search_filter').val()).draw();
    });

    $('.search_filter').keyup(function () { //input의 값대로 search
        let $value = $(this).val();
        $table.columns(Number($('.select_filter').val())).search($value).draw();
    })
});
