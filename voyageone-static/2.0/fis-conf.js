/**
 * Created by edward-pc1 on 2015/10/22.
 */

fis.match('::package', {
   spriter: fis.plugin('csssprites')
});

/**
 * 处理css文件 Start.
 */
// 将Less文件解析成css
fis.match('/static/*.less', {
    parser:fis.plugin('less'),
    rExt: '.css'
});

// 将static下面的所有css文件压缩成一个min.css文件并publish到
fis.match('/static/*.css', {
    optimizer: fis.plugin('clean-css'),
    release: '../publish/${publish_version}/static/${css_version}/$0'
});
/** 处理css文件 End.*/

fis.config.set('publish_version', '2.0.0');
fis.config.set('css_version', '1.0.0');