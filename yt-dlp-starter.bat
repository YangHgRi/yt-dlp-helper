@ECHO OFF

SET TAB=	

ECHO.
ECHO Switch Target URL List:
ECHO.
ECHO %TAB%Youtube=1
ECHO %TAB%Bilibili=2
ECHO %TAB%Pornhub=3
ECHO %TAB%Game=4
ECHO %TAB%ASMR=5
ECHO %TAB%ASMR-Audio=6
ECHO.

SET /p targetList=Target URL List = 

SET config=D:\MediaLib\yt-dlp.conf
SET helper=D:\MediaLib\yt-dlp-helper.jar

FOR /F "tokens=*" %%p IN ('java -jar %helper% "--m=getPath" "--t=%targetList%"') do (SET listPath=%%p)

java -jar %helper% --m=ext --p=%listPath%

FOR /F "tokens=*" %%p IN ('java -jar %helper% "--m=getWorkDir" "--t=%targetList%"') do (SET workDir=%%p)
PUSHD %workDir%

yt-dlp --exec after_move:"java -jar %helper% --m=delURL --p=%listPath% --url=%%(original_url)s --listID=%%(playlist_id)s --listCount=%%(playlist_count)s --listIndex=%%(playlist_index)s" --config-location "%config%" -a "%listPath%"

PAUSE