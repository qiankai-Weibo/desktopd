set cmd_cvs="C:\Program Files\cvsnt\cvs.exe"
cd applet\JDesktopDirectTCS\src
mkdir org

"%ProgramFiles%\winrar\winrar.exe" x -y org.zip -d org
:if not %errorlevel%==0 echo unzip org.zip error && goto end

copy /Y com_desktopdirect_client_wintcsmng.h com\desktopdirect\client
copy /Y ..\array_sun_keystore com\desktopdirect\client
xcopy /S /Y org com\desktopdirect\client
if not %errorlevel%==0 echo copy org error && goto end

rd /S /Q com\desktopdirect\client\com
if not %errorlevel%==0 echo delete com error && goto end



cd com\desktopdirect\tcs
javac -d ..\client\ *.java
if not %errorlevel%==0 echo compile tcs error && goto end

cd ..\client\com
mkdir borland
xcopy /s /e ..\..\..\borland .\borland
if not %errorlevel%==0 echo copy borland error && goto end

cd ..\
:%cmd_cvs% update
javac -d . *.java
if not %errorlevel%==0 echo compile client error. && goto end

copy /Y ..\..\..\..\..\..\..\..\..\output\DDDllClient.dll com\desktopdirect\client
copy /Y ..\..\..\..\..\..\..\..\..\output\DDJNI.dll com\desktopdirect\client
copy /Y ..\..\..\..\..\..\..\..\..\..\..\build\JDesktopDirectTCS\com\desktopdirect\client\start.applescript com\desktopdirect\client
copy /Y ..\..\..\..\..\..\..\..\..\..\..\build\JDesktopDirectTCS\com\desktopdirect\client\DesktopDirect com\desktopdirect\client
jar -cvfm JDesktopDirectTCS.jar ..\..\..\..\..\..\..\..\..\..\..\build\manifest com org com_desktopdirect_client_wintcsmng.h
jarsigner -keystore array_sun_keystore -storepass click1 JDesktopDirectTCS.jar array
copy /Y JDesktopDirectTCS.jar ..\..\..\..\..\..\
cd ..\..\..\..\..\..\
copy /Y JDesktopDirectTCS.jar e:\
cd ..\..\..\..\..\..\
:end
@echo "Batch file dd_build_java.bat end."


