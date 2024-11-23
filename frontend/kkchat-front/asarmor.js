const asarmor = require('asarmor')
const path = require('path')
exports.default = async ({ appOutDir, packager }) => {
  try {
    const asarPath = path.join(packager.getResourcesDir(appOutDir), 'app.asar')
    const archive = await asarmor.open(asarPath)
    archive.patch()
    archive.patch(asarmor.createBloatPatch(1314))
    await archive.write(asarPath)
  } catch (error) {
    console.log(error)
  }
}