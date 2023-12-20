// 비디오 파일 업로드 관련 유효성 검사 코드
function validation(obj){
    const files = obj.files[0];             // 파일 객체 선택
    console.log('obj :' + files);
    const fileTypes = ['video/mp4'];        // 입력될 파일타입
    const mb = 50;                          // 메가 바이트 용량
    const fileSize = (mb * 1024 * 1024);    // 최대 파일 크기
    if (files.name.length > 100) {
        alert("파일명이 100자 이상인 것은 올릴 수 없습니다.");
        obj.value = ''; // 파일 선택 취소
        return false;
    } else if (files.size > fileSize) {
        alert(`${mb}MB를 초과한 파일은 올릴 수 없습니다.`);
        obj.value = ''; // 파일 선택 취소
        return false;
    } else if (files.name.lastIndexOf('.') === -1 || !files.type.includes(fileTypes[0])) {
        alert("파일은 mp4만 올릴 수 있습니다.");
        obj.value = ''; // 파일 선택 취소
        return false;
    } else {
        return true;
    }
}