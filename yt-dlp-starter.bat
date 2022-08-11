@ECHO OFF

SET helper=%yt-dlp-helper%

java -jar %helper% --m=getKeys

SET /p targetKey=Target Key Ordinal = 

FOR /F "tokens=*" %%p IN ('java -jar %helper% "--m=getPath" "--t=%targetKey%"') do (SET listPath=%%p)

java -jar %helper% --m=ext --p=%listPath%

FOR /F "tokens=*" %%p IN ('java -jar %helper% "--m=getWorkDir" "--t=%targetKey%"') do (SET workDir=%%p)
PUSHD %workDir%

FOR /F "tokens=*" %%p IN ('java -jar %helper% "--m=getConfig"') do (SET config=%%p)

yt-dlp --exec after_move:"java -jar %helper% --m=delURL --p=%listPath% --url=%%(original_url)s --listID=%%(playlist_id)s --listCount=%%(playlist_count)s --listIndex=%%(playlist_index)s" --config-location "%config%" -a "%listPath%"

PAUSE