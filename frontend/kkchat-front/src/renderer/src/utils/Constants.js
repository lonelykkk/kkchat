//文件类型
const File_TYPE = {
    jpeg: 0, jpg: 0, png: 0, gif: 0, bmp: 0, webp: 0,
    mp4: 1, avi: 1, rmvb: 1, mkv: 1, mov: 1,
    0: "图片",
    1: "视频",
    2: "文件",
}

const getFileType = (suffix) => {
    if (suffix == undefined) {
        return 2;
    }
    if (typeof suffix === 'string') {
        suffix = suffix.toLowerCase();
    }
    const fileType = File_TYPE[suffix];
    return fileType == undefined ? 2 : fileType;
}
export {
    getFileType
}